package com.iglooit.core.base.iface.validation;

import com.google.gwt.i18n.client.Messages;

public interface ValidationConstants extends Messages
{
    String emailFormatFail();

    String emailFormatRecommendation();

    String minStringLengthFail(int i);

    String maxStringLengthFail(int strlen);

    String phoneNumberFormatFail();

    String fieldRequired();

    String longFail();

    String validationFailedOnServer();

    String userExists(String username);

    String passwordsDontMatch();

    String outOfValidNumberRange(Double min, Double max);

    String invalidDateCompareMsg();

    String invalidNumberFormatOneDigitAfterDot();

    String onlyOneFieldMustPass();

    //language
    String languageEnglish();

    String languageFrench();

    String languageBulgarian();

    String languageRussian();

    String languageVietnamese();

    String languageUKEnglish();

    String passwordDiffError(int passwordDiffChars);

    String passwordInvalidChars();

    String passwordSameAsUsername();

    String invalidPasswords();

    String passwordIsInvalidWord();

    String punctuation();

    String punctuationNumRequired();

    String numbers();

    String numbersNumRequired();

    String letters();

    String lettersNumRequired();

    String passwordComplexity(int requiredLetters, int requiredPunctuation, int requiredNumbers);

    String passwordMinimumLength();

    String passwordDifferentCharsRequired();

    String invalidUsername();

    String usernameFirstMustBeLetter();

    String dateIsBeforeToday();

    String invalidParkedDate();

    String invalidNumber();

    String invalidInteger();

    String invalidInputMessage();

    String positiveIntegerFail();

    String greaterThan();

    String greaterThanEqual();

    String lessThan();

    String lessThanEqual();

    String equalTo();

    String notEqualTo();

    String invalidDateComparison(String firstDateLabel, String operator, String secondDateLabel);

    String firstDateLabel();

    String secondDateLabel();

    String accessProfileNotExist(String profileName);

    String passwordProfileNotExist(String profileName);

    String organizationNotExist(String organizationName);

    String preferredNotificationNotExist(String profileName);
}
