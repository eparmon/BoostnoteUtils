package by.eparmon.boostnoteutils;

import org.junit.Test;

public class BoostnoteUtilsTest {

    @Test
    public void noCommand() {
        BoostnoteUtils.main();
    }

    @Test
    public void unknownCommand() {
        BoostnoteUtils.main("unknown");
    }

    @Test
    public void help() {
        BoostnoteUtils.main("--help");
    }

    @Test
    public void scan() {
        BoostnoteUtils.main("scan");
    }
}
