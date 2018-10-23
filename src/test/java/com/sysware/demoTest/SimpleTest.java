package com.sysware.demoTest;

import com.sysware.enums.PermissionEnum;
import org.junit.Test;

public class SimpleTest {
    @Test
    public void testEnum() {
        String id = PermissionEnum.Edit.getId();

        System.out.println(id);

    }
}
