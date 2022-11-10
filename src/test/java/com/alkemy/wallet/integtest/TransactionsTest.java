package com.alkemy.wallet.integtest;

import com.alkemy.wallet.SpringBaseTest;
import com.alkemy.wallet.configuration.JwtRequestFilter;
import com.alkemy.wallet.model.entity.UserEntity;
import com.alkemy.wallet.repository.BankDAO;
import com.alkemy.wallet.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TransactionsTest extends SpringBaseTest {

    public static final String TRANSACTIONS_DEPOSIT_URL = "/transactions/deposit";

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BankDAO bankDAO;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Test
    public void whenUserTryToCreateTransactionDeposit_thenDepositIsCreated() throws Exception {
        UserEntity userPersisted = getMockUserEntity();

        when(userRepository.findUserByEmail("foo@foo.com")).thenReturn(userPersisted);
        when(bankDAO.findUserByEmail("foo@foo.com")).thenReturn(userPersisted);

        executePost(TRANSACTIONS_DEPOSIT_URL, "transactions", "post-create-valid-transaction", "post-empty", HttpStatus.CREATED.value());
    }

    private UserEntity getMockUserEntity() {
        return UserEntity.builder()
                .userId(1000L)
                .firstName("Foo")
                .lastName("Foo Foo")
                .email("foo@foo.com")
                .password("123456")
                .build();
    }
}
