package s21.main;

import java.util.Scanner;

import s21.main.Main.SettingMenus;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        
        SettingMenus state = SettingMenus.IDLE;
        try (Scanner in = new Scanner(System.in)) {
            final Settings settings = new Settings();
            
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
                    }
                    case DELIEMER -> {
                        settings.deliemer *= 2;
                        if(settings.deliemer > 16) settings.deliemer = 1;
                        System.out.println(state.getName() + " is " + settings.deliemer);
                        state = SettingMenus.IDLE;
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
                                    System.out.println(settings.steps);
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