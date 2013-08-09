/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.rcp.kemecom.model;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author barenko
 */
public class Password {

    public static Password generateRandom(int size) {
        return new Password(RandomStringUtils.randomAlphanumeric(size));
    }
    private String pwd;

    public Password(String password) {
        this.pwd = password;
    }

    public String toString() {
        return pwd == null ? "" : pwd.trim();
    }

    public String toSha512Hex() {
        return pwd == null ? pwd : DigestUtils.sha512Hex(pwd);
    }
}
