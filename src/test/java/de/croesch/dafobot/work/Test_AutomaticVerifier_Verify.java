package de.croesch.dafobot.work;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.croesch.dafobot.work.api.VerificationResult;

/** Provides tests for {@link AutomaticVerifier#verify(SimpleArticle, SimpleArticle)}. */
public class Test_AutomaticVerifier_Verify {

  @Test
  public void should_Always_Return_Good_If_No_Maximum_Set() {
    final AutomaticVerifier verifier = new AutomaticVerifier();
    for (int i = 0; i < 10000000; ++i) {
      assertThat(verifier.verify(null, null)).isEqualTo(VerificationResult.GOOD);
    }
  }

  @Test
  public void should_Always_Return_Good_If_Maximum_Is_Lower_Than_Zero() {
    final AutomaticVerifier verifier = new AutomaticVerifier(-12);
    for (int i = 0; i < 10000000; ++i) {
      assertThat(verifier.verify(null, null)).isEqualTo(VerificationResult.GOOD);
    }
  }

  @Test
  public void should_Return_FATAL_If_Maximum_Is_Zero() {
    final AutomaticVerifier verifier = new AutomaticVerifier(0);
    assertThat(verifier.verify(null, null)).isEqualTo(VerificationResult.FATAL);
  }

  @Test
  public void should_Return_Good_Maximum_Times_And_Then_Return_FATAL() {
    final AutomaticVerifier verifier = new AutomaticVerifier(10);
    for (int i = 0; i < 10; ++i) {
      assertThat(verifier.verify(null, null)).isEqualTo(VerificationResult.GOOD);
    }
    assertThat(verifier.verify(null, null)).isEqualTo(VerificationResult.FATAL);
  }
}
