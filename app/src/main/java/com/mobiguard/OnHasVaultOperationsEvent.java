package com.mobiguard;

/**
 * This sets statues of encrypting in AddLockedVideoFragment. when new intent is fired which call onPause in MAinActivity
 * the event should be sent with appropriate status.
 */
public class OnHasVaultOperationsEvent {

    private boolean hasVaultOperations;

    public OnHasVaultOperationsEvent(boolean hasVaultOperations) {
        this.hasVaultOperations = hasVaultOperations;
    }

    public boolean isHasVaultOperations() {
        return hasVaultOperations;
    }

}
