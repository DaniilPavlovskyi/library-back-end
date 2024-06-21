package com.daniil.library.service;

import com.daniil.library.dao.AuthorityDAO;
import com.daniil.library.entity.Authority;
import com.daniil.library.service.authority.AuthorityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @Mock
    private AuthorityDAO authorityDAO;

    @Test
    void getExistingAuthorityByNameShouldBeOk() {
        Authority mockAuthority = new Authority();
        mockAuthority.setAuthority("user");

        when(authorityDAO.findByAuthority("user")).thenReturn(mockAuthority);

        Authority authority = authorityService.findByName("user");

        verify(authorityDAO, times(1)).findByAuthority("user");
        assertSame(mockAuthority, authority);
    }

    @Test
    void getNotExistingAuthorityByNameShouldBeNull() {
        when(authorityDAO.findByAuthority("user")).thenReturn(null);

        Authority authority = authorityService.findByName("user");
        assertNull(authority);
    }

    @Test
    void saveAuthorityShouldBeOk() {
        Authority authority = new Authority();
        authority.setAuthority("user");

        authorityService.save(authority);

        verify(authorityDAO, times(1)).save(authority);
    }
}
