package s21.main;

public class Settings {
    public boolean direction = false;
    public int deliemer = 1;
    public int speed = 1;
    public volatile int steps = 0;

    public Pinout pinout;

    protected enum Pinout {
        DIRECTION(11),
        STEP(13),
        SLEEP(15),
        RESET(16),
        MS3(18),
        MS2(19),
        MS1(21),
        ENABLE(23);

        private final int pin;

        private Pinout(int pin) {
            this.pin = pin;
        }
        public int getPin() {
            return pin;
        }
    }
}
