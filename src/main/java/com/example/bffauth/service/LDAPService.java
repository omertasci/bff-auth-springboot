package com.example.bffauth.service;

import com.example.bffauth.model.LdapUser;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

@Service
public class LDAPService {

    private final LdapContextSource contextSource;
    private final LdapTemplate ldapTemplate;

    public LDAPService() {
        contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://localhost:389");
        contextSource.setBase("dc=mycompany,dc=com");
        contextSource.setUserDn("cn=admin,dc=mycompany,dc=com");
        contextSource.setPassword("admin");
        contextSource.afterPropertiesSet();

        this.ldapTemplate = new LdapTemplate(contextSource);
    }

    // Kullanıcı bilgilerini getir (opsiyonel)
    public List<LdapUser> searchByUsername(String username) {
        String filter = "uid=" + username;

        return ldapTemplate.search(
                "ou=people",
                filter,
                (AttributesMapper<LdapUser>) this::mapLdapUser
        );
    }

    // LDAP doğrulama (en kritik metod)
    public boolean authenticate(String username, String password) {
        String userDn = "uid=" + username + ",ou=people," + contextSource.getBaseLdapName();

        try {
            LdapContextSource userContext = new LdapContextSource();
            userContext.setUrl(contextSource.getUrls()[0]);
            userContext.setUserDn(userDn);
            userContext.setPassword(password);
            userContext.afterPropertiesSet();

            LdapTemplate userTemplate = new LdapTemplate(userContext);
            // Test bağlantı (basit bir query)
            userTemplate.lookup(""); // root DSE

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private LdapUser mapLdapUser(Attributes attrs) throws NamingException {
        String fullName = (String) attrs.get("cn").get();
        String uid = (String) attrs.get("uid").get();
        String mail = attrs.get("mail") != null ? (String) attrs.get("mail").get() : null;
        return new LdapUser(fullName, uid, mail);
    }
}
