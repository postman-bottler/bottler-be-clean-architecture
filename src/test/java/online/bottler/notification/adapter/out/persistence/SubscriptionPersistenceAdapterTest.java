package online.bottler.notification.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import online.bottler.IdGenerator;
import online.bottler.RedisTestContainersConfig;
import online.bottler.notification.domain.Device;
import online.bottler.notification.domain.Subscription;
import online.bottler.notification.domain.Subscriptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(RedisTestContainersConfig.class)
class SubscriptionPersistenceAdapterTest {
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private SubscriptionPersistenceAdapter subscriptionPersistenceAdapter;

    @DisplayName("사용자의 기기 구독 정보를 저장한다.")
    @Test
    void save() {
        // given
        Subscription subscription = Subscription.create(1L, "token");

        // when
        Subscription save = subscriptionPersistenceAdapter.save(subscription);

        // then
        assertThat(save.getUserId()).isEqualTo(1L);
        assertThat(save.getDevice()).isEqualTo(new Device("token"));
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("사용자의 기기 구독 정보를 조회한다.")
    @Test
    void findByUserId() {
        // given
        Long userId = idGenerator.generateId();
        Subscription subscription1 = Subscription.create(userId, "token1");
        Subscription subscription2 = Subscription.create(userId, "token2");
        subscriptionPersistenceAdapter.save(subscription1);
        subscriptionPersistenceAdapter.save(subscription2);

        // when
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findByUserId(userId);

        // then
        assertThat(subscriptions.getSubscriptions()).hasSize(2)
                .extracting("userId", "device")
                .containsExactlyInAnyOrder(
                        tuple(userId, new Device("token1")),
                        tuple(userId, new Device("token2"))
                );
    }

    @DisplayName("사용자의 구독 정보가 없으면 빈 객체를 반환한다.")
    @Test
    void findByUserIdWithoutSubscription() {
        // given
        Long userId = idGenerator.generateId();

        // when
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findByUserId(userId);

        // then
        assertThat(subscriptions.getSubscriptions()).isEmpty();
    }

    @DisplayName("모든 사용자의 구독 정보를 조회한다.")
    @Test
    void findAll() {
        // given
        long userId1 = idGenerator.generateId();
        Subscription subscription1 = Subscription.create(userId1, "token1");
        long userId2 = idGenerator.generateId();
        Subscription subscription2 = Subscription.create(userId2, "token2");
        subscriptionPersistenceAdapter.save(subscription1);
        subscriptionPersistenceAdapter.save(subscription2);

        // when
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findAll();

        // then
        assertThat(subscriptions.getSubscriptions()).hasSize(2)
                .extracting("userId", "device")
                .containsExactlyInAnyOrder(
                        tuple(userId1, new Device("token1")),
                        tuple(userId2, new Device("token2"))
                );
    }

    @DisplayName("사용자의 구독 정보를 삭제한다.")
    @Test
    void deleteAllByUserId() {
        // given
        Long userId = idGenerator.generateId();
        Subscription subscription = Subscription.create(userId, "token1");
        subscriptionPersistenceAdapter.save(subscription);

        // when
        subscriptionPersistenceAdapter.deleteAllByUserId(userId);

        // then
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findByUserId(userId);
        assertThat(subscriptions.getSubscriptions()).isEmpty();
    }

    @DisplayName("특정 기기의 구독 정보를 삭제한다.")
    @Test
    void deleteByToken() {
        // given
        String token = "token1";
        Device device = new Device("token1");
        long userId = idGenerator.generateId();
        Subscription subscription1 = Subscription.create(userId, token);
        Subscription subscription2 = Subscription.create(userId, "token2");
        subscriptionPersistenceAdapter.save(subscription1);
        subscriptionPersistenceAdapter.save(subscription2);

        // when
        subscriptionPersistenceAdapter.deleteByDevice(userId, device);

        // then
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findByUserId(userId);
        assertThat(subscriptions.getSubscriptions()).hasSize(1)
                .extracting("userId", "device")
                .containsExactlyInAnyOrder(tuple(userId, new Device("token2")));
    }

    @DisplayName("이미 구독된 유저의 기기라면, true를 반환한다.")
    @Test
    void checkDeviceDuplicateWithDuplicateSubscription() {
        // given
        long userId = idGenerator.generateId();
        Subscription subscription = Subscription.create(userId, "token");
        Subscription duplicateSubscription = Subscription.create(userId, "token");
        subscriptionPersistenceAdapter.save(subscription);

        // when
        Boolean result = subscriptionPersistenceAdapter.checkDeviceDuplicate(duplicateSubscription.getDevice());

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("저장되지 않은 구독 정보라면, false를 반환한다.")
    @Test
    void checkDeviceDuplicate() {
        // given
        Long userId = idGenerator.generateId();
        Subscription subscription = Subscription.create(userId, "token1");
        Subscription notDuplicateSubscription = Subscription.create(userId, "token2");
        subscriptionPersistenceAdapter.save(subscription);

        // when
        Boolean result = subscriptionPersistenceAdapter.checkDeviceDuplicate(notDuplicateSubscription.getDevice());

        // then
        assertThat(result).isFalse();
    }
}
