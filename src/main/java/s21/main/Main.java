package s21.main;

import java.util.Scanner;

import com.pi4j.Pi4J;
import com.pi4j.platform.Platforms;

import s21.main.Settings.Pinout;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final Settings settings = new Settings();

        var pi4j = Pi4J.newAutoContext();
        Platforms platforms = pi4j.platforms();
        
        var direction_pin = pi4j.digitalOutput().create(Pinout.DIRECTION.getPin());
        var step_pin = pi4j.digitalOutput().create(Pinout.STEP.getPin());
        var ms3_pin = pi4j.digitalOutput().create(Pinout.MS3.getPin());
        var ms2_pin = pi4j.digitalOutput().create(Pinout.MS2.getPin());
        var ms1_pin = pi4j.digitalOutput().create(Pinout.MS1.getPin());

        SettingMenus state = SettingMenus.IDLE;
        try (Scanner in = new Scanner(System.in)) {
            while(state != SettingMenus.EXIT) {
                switch(state) {
                    case IDLE -> {
                        for(int i = 1; i < SettingMenus.values().length; i++) {
                            System.out.println(i + ". " + SettingMenus.values()[i].getName());
                        }
                        int menu = in.nextInt();
                        if(menu >= 1 && menu < SettingMenus.values().length)
                            state = SettingMenus.values()[menu];
                    }
                    case DIRECTION -> {
                        settings.direction = !settings.direction;
                        System.out.println("Direction is: " + (settings.direction == false ? "left" : "right"));
                        state = SettingMenus.IDLE;
                        if(settings.direction == false) direction_pin.high();
                        else direction_pin.low();
                    }
                    case DELIEMER -> {
                        settings.deliemer *= 2;
                        if(settings.deliemer > 16) settings.deliemer = 1;
                        System.out.println(state.getName() + " is " + settings.deliemer);
                        state = SettingMenus.IDLE;
                        switch(settings.deliemer) {
                            case 1 -> {
                                ms1_pin.low();
                                ms2_pin.low();
                                ms3_pin.low();
                            }
                            case 2 -> {
                                ms1_pin.high();
                                ms2_pin.low();
                                ms3_pin.low();
                            }
                            case 4 -> {
                                ms1_pin.low();
                                ms2_pin.high();
                                ms3_pin.low();
                            }
                            case 8 -> {
                                ms1_pin.high();
                                ms2_pin.high();
                                ms3_pin.low();
                            }
                            case 16 -> {
                                ms1_pin.high();
                                ms2_pin.high();
                                ms3_pin.high();
                            }
                        }
                    }
                    case STEP -> {
                        System.out.println("Insert steps count");
                        int count = in.nextInt();

                        if(count < 0) count = 0;

                        if(settings.steps == 0) {
                            settings.steps = count;
                            Thread stepsThread = new Thread(()->{
                                while(settings.steps > 0) {
                                    //step
                                    step_pin.high();
                                    step_pin.low();
                                    try {
                                        Thread.sleep(1000 / settings.speed);
                                    } catch (InterruptedException e) {
                                        System.err.println(e.getMessage());
                                    }
                                    settings.steps--;
                                }
                            }, "Steps Thread");
                            stepsThread.start();
                        } else settings.steps = count;
                        state = SettingMenus.IDLE;
                    }
                    case SPEED -> {
                        System.out.println("Insert steps count in second");
                        int speed = in.nextInt();

                        if(speed < 0 || speed > 100) speed = 1;
                        settings.speed = speed;

                        state = SettingMenus.IDLE;
                    }
                    case EXIT -> {}
                }
            }
        }
        pi4j.shutdown();
    }
    protected enum SettingMenus {
        IDLE("Idle"),
        DIRECTION("Direction"),
        DELIEMER("Deliemer"),
        STEP("Step"),
        SPEED("Speed"),
        EXIT("Exit");

        private final String name;

        SettingMenus(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }
    
}