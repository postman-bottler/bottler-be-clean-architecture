package online.bottler.notification.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import online.bottler.notification.adapter.out.persistence.SubscriptionPersistenceAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.notification.domain.Subscription;
import online.bottler.notification.domain.Subscriptions;

@SpringBootTest
@Transactional
class SubscriptionPersistenceAdapterTest {

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
        assertThat(save.getToken()).isEqualTo("token");
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("사용자의 기기 구독 정보를 조회한다.")
    @Test
    void findByUserId() {
        // given
        Long userId = 1L;
        Subscription subscription1 = Subscription.create(userId, "token1");
        Subscription subscription2 = Subscription.create(userId, "token2");
        subscriptionPersistenceAdapter.save(subscription1);
        subscriptionPersistenceAdapter.save(subscription2);

        // when
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findByUserId(1L);

        // then
        assertThat(subscriptions.getSubscriptions()).hasSize(2)
                .extracting("userId", "token")
                .containsExactlyInAnyOrder(
                        tuple(1L, "token1"),
                        tuple(1L, "token2")
                );
    }

    @DisplayName("사용자의 구독 정보가 없으면 빈 객체를 반환한다.")
    @Test
    void findByUserIdWithoutSubscription() {
        // given
        Long userId = 1L;

        // when
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findByUserId(userId);

        // then
        assertThat(subscriptions.getSubscriptions()).isEmpty();
    }

    @DisplayName("모든 사용자의 구독 정보를 조회한다.")
    @Test
    void findAll() {
        // given
        Subscription subscription1 = Subscription.create(1L, "token1");
        Subscription subscription2 = Subscription.create(2L, "token2");
        subscriptionPersistenceAdapter.save(subscription1);
        subscriptionPersistenceAdapter.save(subscription2);

        // when
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findAll();

        // then
        assertThat(subscriptions.getSubscriptions()).hasSize(2)
                .extracting("userId", "token")
                .containsExactlyInAnyOrder(
                        tuple(1L, "token1"),
                        tuple(2L, "token2")
                );
    }

    @DisplayName("사용자의 구독 정보를 삭제한다.")
    @Test
    void deleteAllByUserId() {
        // given
        Long userId = 1L;
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
        Subscription subscription1 = Subscription.create(1L, token);
        Subscription subscription2 = Subscription.create(1L, "token2");
        subscriptionPersistenceAdapter.save(subscription1);
        subscriptionPersistenceAdapter.save(subscription2);

        // when
        subscriptionPersistenceAdapter.deleteByToken(token);

        // then
        Subscriptions subscriptions = subscriptionPersistenceAdapter.findByUserId(1L);
        assertThat(subscriptions.getSubscriptions()).hasSize(1)
                .extracting("userId", "token")
                .containsExactlyInAnyOrder(tuple(1L, "token2"));
    }

    @DisplayName("이미 구독된 유저의 기기라면, true를 반환한다.")
    @Test
    void isDuplicateWithDuplicateSubscription() {
        // given
        Subscription subscription = Subscription.create(1L, "token");
        Subscription duplicateSubscription = Subscription.create(1L, "token");
        subscriptionPersistenceAdapter.save(subscription);

        // when
        Boolean result = subscriptionPersistenceAdapter.isDuplicate(duplicateSubscription);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("저장되지 않은 구독 정보라면, false를 반환한다.")
    @Test
    void isDuplicate() {
        // given
        Subscription subscription = Subscription.create(1L, "token1");
        Subscription notDuplicateSubscription = Subscription.create(1L, "token2");
        subscriptionPersistenceAdapter.save(subscription);

        // when
        Boolean result = subscriptionPersistenceAdapter.isDuplicate(notDuplicateSubscription);

        // then
        assertThat(result).isFalse();
    }
}
