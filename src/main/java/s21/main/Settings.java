package s21.main;

public class Settings {
    public boolean direction = false;
    public int deliemer = 1;
    public int speed = 1;
    public volatile int steps = 0;

    public Pinout pinout;

    protected enum Pinout {
        DIRECTION(0),
        STEP(0),
        SLEEP(0),
        RESET(0),
        MS3(0),
        MS2(0),
        MS1(0),
        ENABLE(0);

        private final int pin;

        private Pinout(int pin) {
            this.pin = pin;
        }
        public int getPin() {
            return pin;
        }
    }
}
