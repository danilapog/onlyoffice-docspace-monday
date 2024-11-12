package com.onlyoffice.common.service.encryption;

public interface EncryptionService {
  String encrypt(String plainText) throws EncryptionException;

  String decrypt(String cipherText) throws DecryptionException;
}
