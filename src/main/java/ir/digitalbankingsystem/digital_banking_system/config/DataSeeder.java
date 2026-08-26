package ir.digitalbankingsystem.digital_banking_system.config;

import ir.digitalbankingsystem.digital_banking_system.domain.*;
import ir.digitalbankingsystem.digital_banking_system.repository.AccountRepository;
import ir.digitalbankingsystem.digital_banking_system.repository.PersonRepository;
import ir.digitalbankingsystem.digital_banking_system.repository.TransactionRepository;
import ir.digitalbankingsystem.digital_banking_system.service.CardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            PersonRepository personRepository,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder,
            CardService cardService
    ) {

        return args -> {

            // اگر قبلاً داده ساخته شده، دوباره نساز
            if (personRepository.count() > 0) {
                return;
            }


            /*
             * =========================
             * ADMINS
             * =========================
             */

            Admin admin1 = new Admin();

            admin1.setFirstName("Admin");
            admin1.setLastName("One");
            admin1.setUserName("admin1");
            admin1.setPassword(
                    passwordEncoder.encode("123456")
            );
            admin1.setRole(Role.ADMIN);
            admin1.setGender(Gender.MALE);
            admin1.setAge(30);
            admin1.setStatus(Status.APPROVED);
            admin1.setAddress("belabela");
            admin1.setEmail("mieow@gmail.com");
            admin1.setPhoneNumber("1234567890");


            Admin admin2 = new Admin();

            admin2.setFirstName("Admin");
            admin2.setLastName("Two");
            admin2.setUserName("admin2");
            admin2.setPassword(
                    passwordEncoder.encode("123456")
            );
            admin2.setRole(Role.ADMIN);
            admin2.setGender(Gender.FEMALE);
            admin2.setAge(35);
            admin2.setStatus(Status.APPROVED);
            admin2.setAddress("belabefla");
            admin2.setEmail("mieowk@gmail.com");
            admin2.setPhoneNumber("12345687890");



            /*
             * =========================
             * CUSTOMERS
             * =========================
             */

            Customer customer1 = new Customer();

            customer1.setFirstName("Hossein");
            customer1.setLastName("Customer");
            customer1.setUserName("customer1");
            customer1.setPassword(
                    passwordEncoder.encode("123456")
            );
            customer1.setRole(Role.CUSTOMER);
            customer1.setGender(Gender.MALE);
            customer1.setAge(20);
            customer1.setStatus(Status.APPROVED);
            customer1.setAddress("bealabela");
            customer1.setEmail("mieow@gkjmail.com");
            customer1.setPhoneNumber("123904567890");


            Customer customer2 = new Customer();

            customer2.setFirstName("Ali");
            customer2.setLastName("Customer");
            customer2.setUserName("customer2");
            customer2.setPassword(
                    passwordEncoder.encode("123456")
            );
            customer2.setRole(Role.CUSTOMER);
            customer2.setGender(Gender.MALE);
            customer2.setAge(25);
            customer2.setStatus(Status.APPROVED);
            customer2.setAddress("bealafdsbela");
            customer2.setEmail("miehjkow@gmail.com");
            customer2.setPhoneNumber("12345098767890");


            Customer customer3 = new Customer();

            customer3.setFirstName("Sara");
            customer3.setLastName("Customer");
            customer3.setUserName("customer3");
            customer3.setPassword(
                    passwordEncoder.encode("123456")
            );
            customer3.setRole(Role.CUSTOMER);
            customer3.setGender(Gender.FEMALE);
            customer3.setAge(24);
            customer3.setStatus(Status.APPROVED);
            customer3.setAddress("bealdadaafdsbela");
            customer3.setEmail("qkwfdjkfwdh@gmail.com");
            customer3.setPhoneNumber("12987634567890");


            /*
             * =========================
             * EMPLOYEES
             * =========================
             */

            Employee employee1 = new Employee();

            employee1.setFirstName("Employee");
            employee1.setLastName("One");
            employee1.setUserName("employee1");
            employee1.setPassword(
                    passwordEncoder.encode("123456")
            );
            employee1.setRole(Role.EMPLOYEE);
            employee1.setGender(Gender.MALE);
            employee1.setAge(28);
            employee1.setStatus(Status.APPROVED);
            employee1.setAddress("bealafdsbefsfdsla");
            employee1.setEmail("fkjdhjksdhqq@gmail.com");
            employee1.setPhoneNumber("47849387337");


            Employee employee2 = new Employee();

            employee2.setFirstName("Employee");
            employee2.setLastName("Two");
            employee2.setUserName("employee2");
            employee2.setPassword(
                    passwordEncoder.encode("123456")
            );
            employee2.setRole(Role.EMPLOYEE);
            employee2.setGender(Gender.FEMALE);
            employee2.setAge(32);
            employee2.setStatus(Status.APPROVED);
            employee2.setAddress("bealafdsbhhdefsfdsla");
            employee2.setEmail("miekjhddttow@gmail.com");
            employee2.setPhoneNumber("1234575471890");


            /*
             * =========================
             * SAVE PERSONS
             * =========================
             */

            personRepository.saveAll(
                    List.of(
                            admin1,
                            admin2,
                            customer1,
                            customer2,
                            customer3,
                            employee1,
                            employee2
                    )
            );


            /*
             * =========================
             * CREATE ACCOUNTS
             * =========================
             */

            Account customer1Account1 =
                    createAccount(
                            "1000000001",
                            customer1
                    );

            Account customer1Account2 =
                    createAccount(
                            "1000000002",
                            customer1
                    );

            Account customer2Account =
                    createAccount(
                            "1000000003",
                            customer2
                    );

            Account customer3Account =
                    createAccount(
                            "1000000004",
                            customer3
                    );

            Account employee1Account =
                    createAccount(
                            "2000000001",
                            employee1
                    );

            Account employee2Account =
                    createAccount(
                            "2000000002",
                            employee2
                    );


            accountRepository.saveAll(
                    List.of(
                            customer1Account1,
                            customer1Account2,
                            customer2Account,
                            customer3Account,
                            employee1Account,
                            employee2Account
                    )
            );

            // Issue one active card for every seeded account.
            cardService.issueCard(customer1Account1.getAccountNumber());
            cardService.issueCard(customer1Account2.getAccountNumber());
            cardService.issueCard(customer2Account.getAccountNumber());
            cardService.issueCard(customer3Account.getAccountNumber());
            cardService.issueCard(employee1Account.getAccountNumber());
            cardService.issueCard(employee2Account.getAccountNumber());


            /*
             * =========================
             * DEPOSIT TRANSACTIONS
             * =========================
             */

            Transaction deposit1 =
                    createTransaction(
                            null,
                            customer1Account1,
                            new BigDecimal("10000"),
                            Type.DEPOSIT,
                            TransactionStatus.SUCCESS
                    );


            Transaction deposit2 =
                    createTransaction(
                            null,
                            customer1Account2,
                            new BigDecimal("5000"),
                            Type.DEPOSIT,
                            TransactionStatus.SUCCESS
                    );


            Transaction deposit3 =
                    createTransaction(
                            null,
                            customer2Account,
                            new BigDecimal("20000"),
                            Type.DEPOSIT,
                            TransactionStatus.SUCCESS
                    );


            Transaction deposit4 =
                    createTransaction(
                            null,
                            customer3Account,
                            new BigDecimal("15000"),
                            Type.DEPOSIT,
                            TransactionStatus.SUCCESS
                    );


            /*
             * =========================
             * TRANSFERS
             * =========================
             */

            Transaction transfer1 =
                    createTransaction(
                            customer1Account1,
                            customer2Account,
                            new BigDecimal("2000"),
                            Type.TRANSFER,
                            TransactionStatus.SUCCESS
                    );


            Transaction transfer2 =
                    createTransaction(
                            customer2Account,
                            customer1Account2,
                            new BigDecimal("3500"),
                            Type.TRANSFER,
                            TransactionStatus.SUCCESS
                    );


            Transaction transfer3 =
                    createTransaction(
                            customer1Account1,
                            customer3Account,
                            new BigDecimal("1000"),
                            Type.TRANSFER,
                            TransactionStatus.SUCCESS
                    );


            Transaction transfer4 =
                    createTransaction(
                            customer3Account,
                            employee1Account,
                            new BigDecimal("2500"),
                            Type.TRANSFER,
                            TransactionStatus.SUCCESS
                    );


            Transaction transfer5 =
                    createTransaction(
                            employee1Account,
                            employee2Account,
                            new BigDecimal("700"),
                            Type.TRANSFER,
                            TransactionStatus.SUCCESS
                    );


            /*
             * =========================
             * WITHDRAW
             * =========================
             */

            Transaction withdraw1 =
                    createTransaction(
                            customer1Account2,
                            null,
                            new BigDecimal("500"),
                            Type.WITHDRAW,
                            TransactionStatus.SUCCESS
                    );


            Transaction withdraw2 =
                    createTransaction(
                            customer2Account,
                            null,
                            new BigDecimal("1000"),
                            Type.WITHDRAW,
                            TransactionStatus.SUCCESS
                    );


            /*
             * =========================
             * FAILED TRANSACTIONS
             * =========================
             */

            Transaction failedTransfer =
                    createTransaction(
                            customer1Account1,
                            customer2Account,
                            new BigDecimal("50000"),
                            Type.TRANSFER,
                            TransactionStatus.FAILED
                    );


            Transaction pendingTransfer =
                    createTransaction(
                            customer2Account,
                            customer3Account,
                            new BigDecimal("800"),
                            Type.TRANSFER,
                            TransactionStatus.PENDING
                    );


            transactionRepository.saveAll(
                    List.of(
                            deposit1,
                            deposit2,
                            deposit3,
                            deposit4,

                            transfer1,
                            transfer2,
                            transfer3,
                            transfer4,
                            transfer5,

                            withdraw1,
                            withdraw2,

                            failedTransfer,
                            pendingTransfer
                    )
            );


            /*
             * =========================
             * UPDATE BALANCES
             * =========================
             */

            customer1Account1.setBalance(
                    new BigDecimal("6500")
            );

            customer1Account2.setBalance(
                    new BigDecimal("8000")
            );

            customer2Account.setBalance(
                    new BigDecimal("17500")
            );

            customer3Account.setBalance(
                    new BigDecimal("11500")
            );

            employee1Account.setBalance(
                    new BigDecimal("1800")
            );

            employee2Account.setBalance(
                    new BigDecimal("700")
            );


            accountRepository.saveAll(
                    List.of(
                            customer1Account1,
                            customer1Account2,
                            customer2Account,
                            customer3Account,
                            employee1Account,
                            employee2Account
                    )
            );


            System.out.println(
                    "================================="
            );

            System.out.println(
                    "TEST DATA CREATED SUCCESSFULLY"
            );

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ADMIN 1: admin1 / 123456"
            );

            System.out.println(
                    "ADMIN 2: admin2 / 123456"
            );

            System.out.println(
                    "CUSTOMER 1: customer1 / 123456"
            );

            System.out.println(
                    "CUSTOMER 2: customer2 / 123456"
            );

            System.out.println(
                    "CUSTOMER 3: customer3 / 123456"
            );

            System.out.println(
                    "EMPLOYEE 1: employee1 / 123456"
            );

            System.out.println(
                    "EMPLOYEE 2: employee2 / 123456"
            );
        };
    }


    private Account createAccount(
            String accountNumber,
            Person person
    ) {

        Account account = new Account();

        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(Currency.IRR);
        account.setStatus(AccountStatus.ACTIVE);
        account.setPerson(person);

        return account;
    }


    private Transaction createTransaction(
            Account fromAccount,
            Account toAccount,
            BigDecimal amount,
            Type type,
            TransactionStatus status
    ) {

        Transaction transaction = new Transaction();

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(status);

        return transaction;
    }
}