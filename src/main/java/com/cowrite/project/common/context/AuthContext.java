package com.cowrite.project.common.context;


import com.cowrite.project.model.entity.User;

/**
 * This class provides a way to store and manage authentication-related context
 * for the current thread, specifically the currently authenticated user and the
 * authentication token (e.g., JWT token). It uses `ThreadLocal` to ensure
 * that each thread has its own isolated context, which is critical in a multithreaded
 * environment, such as web servers where each request is handled by a separate thread.
 *
 * @author heathcetide
 */
public class AuthContext {

    // ThreadLocal variable to store the current authenticated user for each thread.
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    // ThreadLocal variable to store the current authentication token (e.g., JWT token) for each thread.
    private static final ThreadLocal<String> currentToken = new ThreadLocal<>();

    /**
     * Sets the current user in the thread-local context.
     * This method is typically called when a user is authenticated, and their details
     * need to be made available throughout the lifecycle of the current request.
     *
     * @param user the currently authenticated user
     */
    public static void setCurrentUser(User user) {
        currentUser.set(user);  // Set the current user for the current thread
    }

    /**
     * Gets the current user from the thread-local context.
     * This method can be called throughout the lifecycle of the current request
     * to retrieve the authenticated user details.
     *
     * @return the currently authenticated user
     */
    public static User getCurrentUser() {
        return currentUser.get();  // Get the current user for the current thread
    }

    /**
     * Sets the current authentication token (e.g., JWT token) in the thread-local context.
     * This method is typically called when a user is authenticated and their token
     * needs to be associated with the current request.
     *
     * @param token the authentication token (e.g., JWT token) for the user
     */
    public static void setCurrentToken(String token) {
        currentToken.set(token);  // Set the current authentication token for the current thread
    }

    /**
     * Gets the current authentication token (e.g., JWT token) from the thread-local context.
     * This method can be called to retrieve the authentication token associated with
     * the current user for the current request.
     *
     * @return the current authentication token
     */
    public static String getCurrentToken() {
        return currentToken.get();  // Get the current authentication token for the current thread
    }

    /**
     * Clears the current user and authentication token from the thread-local context.
     * This method should be called after the request has been completed (e.g., in a
     * finally block or at the end of a request handling cycle) to ensure that the
     * thread-local variables do not leak memory or hold incorrect information.
     */
    public static void clear() {
        currentUser.remove();  // Clear the current user for the current thread
        currentToken.remove();  // Clear the current authentication token for the current thread
    }
}
