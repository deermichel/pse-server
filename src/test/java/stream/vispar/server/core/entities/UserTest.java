/**
 * 
 */
package stream.vispar.server.core.entities;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link User}.
 * 
 * @author Micha Hanselmann
 */
public class UserTest {

    /**
     * Test method for {@link User#hashCode()}.
     */
    @Test
    public void testHashCode() {
        assertThat(new User("123", "").hashCode(), equalTo(new User("123", "").hashCode()));
    }

    /**
     * Test method for {@link User#getName()}.
     */
    @Test
    public void testGetName() {
        assertThat(new User("user", "").getName(), equalTo("user"));
    }

    /**
     * Test method for {@link User#checkPassword(java.lang.String)}.
     */
    @Test
    public void testCheckPassword() {
        assertThat(new User("user", "123").checkPassword("123"), equalTo(true));
        assertThat(new User("user", "123").checkPassword("132"), equalTo(false));
    }

    /**
     * Test method for {@link User#equals(java.lang.Object)}.
     */
    @Test
    public void testEqualsObject() {
        assertThat(new User("123", ""), equalTo(new User("123", "a")));
        assertThat(new User("123", ""), not(equalTo(new User("a123", ""))));
        assertThat(new User("123", ""), not(equalTo(null)));
    }
}
