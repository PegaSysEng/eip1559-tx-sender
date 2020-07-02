package tech.pegasys.net.core.dagger;

import dagger.Module;
import dagger.Provides;
import tech.pegasys.net.api.AccountRepository;
import tech.pegasys.net.api.CredentialsRepository;
import tech.pegasys.net.api.EIP1559TransactionCreator;
import tech.pegasys.net.api.LegacyTransactionCreator;
import tech.pegasys.net.api.Reporter;
import tech.pegasys.net.cli.Options;
import tech.pegasys.net.config.ChainFillerConfiguration;
import tech.pegasys.net.core.account.AccountRepositoryFactory;
import tech.pegasys.net.core.credentials.CredentialsRepositoryFactory;
import tech.pegasys.net.core.report.ReporterService;
import tech.pegasys.net.core.tx.EIP1559TransactionCreatorService;
import tech.pegasys.net.core.tx.LegacyTransactionCreatorService;

import javax.inject.Singleton;

@Module
public class ChainFillerModule {

  @Provides
  @Singleton
  public static ChainFillerConfiguration chainFillerConfiguration() {
    return Options.getInstance().toTxSenderConfiguration();
  }

  @Provides
  @Singleton
  public static CredentialsRepository credentialsRepository(
      final ChainFillerConfiguration configuration) {
    return CredentialsRepositoryFactory.inMemoryCredentialsRepository(
        configuration.accountPrivateKeys());
  }

  @Provides
  @Singleton
  public static AccountRepository accountRepository(final ChainFillerConfiguration configuration) {
    return AccountRepositoryFactory.inMemoryAccountRepository(configuration.recipientAddresses());
  }

  @Provides
  @Singleton
  public static LegacyTransactionCreator legacyTransactionCreator(
      final AccountRepository accountRepository) {
    return new LegacyTransactionCreatorService(accountRepository);
  }

  @Provides
  @Singleton
  public static EIP1559TransactionCreator eip1559TransactionCreator(
      final AccountRepository accountRepository) {
    return new EIP1559TransactionCreatorService(accountRepository);
  }

  @Provides
  @Singleton
  public static Reporter reporter() {
    return new ReporterService();
  }
}