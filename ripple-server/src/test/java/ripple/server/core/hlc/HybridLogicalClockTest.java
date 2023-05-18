// Copyright (c) 2023 Institute of Software, Chinese Academy of Sciences
// Ripple is licensed under Mulan PSL v2.
// You can use this software according to the terms and conditions of the Mulan PSL v2.
// You may obtain a copy of Mulan PSL v2 at:
//          http://license.coscl.org.cn/MulanPSL2
// THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// See the Mulan PSL v2 for more details.

package ripple.server.core.hlc;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Zhen Tang
 */
public class HybridLogicalClockTest {
    @Test
    public void testGenerateForSending() throws Exception {
        HybridLogicalClockCoordinator coordinator = new HybridLogicalClockCoordinator(100);
        HybridLogicalClock timestampOne = coordinator.generateForSending();
        HybridLogicalClock timestampTwo = coordinator.generateForSending();
        Thread.sleep(150);
        HybridLogicalClock timestampThree = coordinator.generateForSending();
        Assert.assertTrue(timestampOne.smallerThan(timestampTwo));
        Assert.assertTrue(coordinator.isHappenBefore(timestampOne, timestampTwo));
        Assert.assertFalse(coordinator.isConcurrent(timestampOne, timestampTwo));
        Assert.assertTrue(coordinator.isHappenBefore(timestampOne, timestampThree));
        Assert.assertTrue(coordinator.isHappenBefore(timestampTwo, timestampThree));
    }

    @Test
    public void testGenerateForReceiving() throws Exception {
        HybridLogicalClockCoordinator coordinatorOne = new HybridLogicalClockCoordinator(100);
        HybridLogicalClockCoordinator coordinatorTwo = new HybridLogicalClockCoordinator(100);
        HybridLogicalClock timestampOne = coordinatorOne.generateForSending();
        HybridLogicalClock timestampTwo = coordinatorTwo.generateForSending();
        HybridLogicalClock timestampThree = coordinatorTwo.generateForReceiving(timestampOne);
        HybridLogicalClock timestampFour = coordinatorOne.generateForReceiving(timestampTwo);
        Assert.assertTrue(coordinatorOne.isHappenBefore(timestampOne, timestampFour));
        Assert.assertTrue(coordinatorOne.isHappenBefore(timestampTwo, timestampThree));
        Assert.assertTrue(coordinatorOne.isConcurrent(timestampOne, timestampTwo));
        Assert.assertTrue(coordinatorOne.isConcurrent(timestampThree, timestampFour));
    }

    @Test
    public void testHappenBefore() throws Exception {
        HybridLogicalClockCoordinator coordinatorOne = new HybridLogicalClockCoordinator(100);
        HybridLogicalClockCoordinator coordinatorTwo = new HybridLogicalClockCoordinator(100);
        HybridLogicalClock timestampOne = coordinatorOne.generateForSending();
        HybridLogicalClock timestampTwo = coordinatorTwo.generateForSending();
        Thread.sleep(150);
        HybridLogicalClock timestampThree = coordinatorOne.generateForSending();
        HybridLogicalClock timestampFour = coordinatorTwo.generateForSending();
        Assert.assertTrue(coordinatorOne.isHappenBefore(timestampOne, timestampThree));
        Assert.assertTrue(coordinatorOne.isHappenBefore(timestampOne, timestampFour));
        Assert.assertTrue(coordinatorOne.isHappenBefore(timestampTwo, timestampThree));
        Assert.assertTrue(coordinatorOne.isHappenBefore(timestampTwo, timestampFour));
        Assert.assertTrue(coordinatorOne.isConcurrent(timestampOne, timestampTwo));
        Assert.assertTrue(coordinatorOne.isConcurrent(timestampThree, timestampFour));
    }
}
