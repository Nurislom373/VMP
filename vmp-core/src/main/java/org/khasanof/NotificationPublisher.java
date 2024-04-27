package org.khasanof;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 4/27/2024 11:49 AM
 */
public interface NotificationPublisher<T> {

    void sendNotification(Object not, T params);
}
