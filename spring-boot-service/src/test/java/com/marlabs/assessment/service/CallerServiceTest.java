package com.marlabs.assessment.service;

import com.marlabs.assessment.exception.UnknownCallerException;
import com.marlabs.assessment.model.Caller;
import com.marlabs.assessment.repository.CallerRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallerServiceTest {

    private final CallerService callerService =
            new CallerService(new CallerRepository());

    @Test
    void shouldResolveAtlasEmployee() {

        Caller caller =
                callerService.resolveCaller("atlas-employee-01");

        assertEquals("atlas-employee-01", caller.callerId());
        assertEquals("Atlas", caller.tenant());
        assertEquals("employee", caller.role());
    }

    @Test
    void shouldResolveAtlasContractor() {

        Caller caller =
                callerService.resolveCaller("atlas-contractor-01");

        assertEquals("Atlas", caller.tenant());
        assertEquals("contractor", caller.role());
    }

    @Test
    void shouldResolveBorealEmployee() {

        Caller caller =
                callerService.resolveCaller("boreal-employee-01");

        assertEquals("Boreal", caller.tenant());
        assertEquals("employee", caller.role());
    }

    @Test
    void shouldRejectUnknownCaller() {

        assertThrows(
                UnknownCallerException.class,
                () -> callerService.resolveCaller("unknown-user")
        );
    }

    @Test
    void shouldRejectMissingCaller() {

        assertThrows(
                UnknownCallerException.class,
                () -> callerService.resolveCaller(null)
        );
    }

    @Test
    void shouldRejectBlankCaller() {

        assertThrows(
                UnknownCallerException.class,
                () -> callerService.resolveCaller("   ")
        );
    }
}