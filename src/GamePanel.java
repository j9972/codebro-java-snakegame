import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

// TODO : todolist 먼저 체크 하고 고치기
// TODO : 게임이 끝난후 재시작할 버튼이랑, 점수 등록하기 -> 버튼 만드는 방법이랑 버튼에 action 주는 방법 찾아보기
// TODO : 처음에 지금 point 가 나타나지 않는데 이거 해결하기
// TODO : point들이 중간중간에 사라지는데 newPoint 메소드 확인


public class GamePanel extends JPanel implements ActionListener {

    // 클래스(전역) 상수
    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50; // 스크린에 보일 격자칸의 크기
    static final int GAME_UNITES = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100; // 지연 시간

    // array 만듬 -> snake 의 헤드를 포함한 위치를 나타내기위함
    final int x[] = new int[GAME_UNITES]; // x 좌표
    final int y[] = new int[GAME_UNITES]; // y 좌표
    int bodyParts = 6; // 기본크기
    int pointEaten; // 처음의 점수는 당연히 0이다
    int pointX; // 점수가 있을 장소의 x 좌표
    int pointY; // 점수가 있을 장소의 y 좌표
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    // panel 의 기본 생성자
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        // 레이아웃 관리자가있는 경우 예상대로 구성 요소를 레이아웃합니다.
        // Dimension 클래스는 단일의 객체내의 컴퍼넌트의 폭과 높이를 정수 정밀도로를 캡슐화합니다 ->  Dimension 을 구축해, 지정된 폭과 높이에 초기화합니다.
        this.setBackground(Color.BLACK);
        this.setFocusable(true); // 키 이벤트의 포터스를 받을 수 있는 컴포턴트가 여러개 있을때 우선적으로 입력받기 위해 설.
        // 이렇게 하면 컴포넌트로부터 먼저 키를 입력 받을 수 있다.
        this.addKeyListener(new MyKeyAdapter()); // 키 이벤트를 듣고 반응하도록 객체를 등록하는 방식
        startGame();
    }

    // 게임 시작 메소드
    public void startGame() {
        newPoint();
        running = true;
        timer = new Timer(DELAY, this); // listener 로 this 사용
        timer.start();

    }

    // 기본 구조 색 칠하는 메소드
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // JPanel 을 위해 paintComponent 을 수행하는 것
        draw(g);
    }

    // 기본
    public void draw(Graphics g) {
        if(running) {        // screen 에 격자형을 만들어서 개발시 시각적으로 확인하기 위함
            /* 개발후에는 격자형이 있을 이유가 없다.
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            */
            g.setColor(Color.RED);
            g.fillOval(pointX, pointY, UNIT_SIZE, UNIT_SIZE);

            // snake 의 시작의 위치를 표시 + snake 의 몸 길이를 표시
            for (int i = 0; i < bodyParts; i++) {
                // i == 0 는 뱀의 시작하는 부분의 색을 의미한다
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor((new Color(45, 180, 0))); // 뱀 몸통색을 초록색으로 고정
                    // snake 의 몸통 색을 여러가지 색으로 표현하는 방법
                    // g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // Game Over text
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));

            // font 를 적용하는데 있어서 사용한다
            FontMetrics metrics = getFontMetrics(g.getFont());

            // 점수를 동적으로 표현
            g.drawString("Score: " + pointEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + pointEaten))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    // 새롭게 점수 포인트 만들기
    public void newPoint() {
        pointX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        pointY = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
    }

    // 뱀이 움직이게 하는 메소드
    public void move() {
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    // 점수를 얻는 부분의 메소드
    public void checkPoint() {
        // 점수를 먹으면 길이 길게해주고 점수 추가
        if((x[0] == pointX) && (y[0] == pointY)) {
            bodyParts++;
            pointEaten++;
            newPoint();
        }
    }

    // 점수먹을때의 메소드
    public void checkCollisions() {
        // 머리가 몸이랑 부디치는지 확인
        for (int i = bodyParts; i>0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // 머리가 왼쪽 벽이랑 부디 치는 경우
        if(x[0] < 0) {
            running = false;
        }

        // 머리가 오른쪽 벽이랑 부디 치는 경우
        if(x[0] > SCREEN_WIDTH) {
            running = false;
        }

        // 머리가 위쪽 벽이랑 부디 치는 경우
        if(y[0] < 0) {
            running = false;
        }

        // 머리가 아래쪽 벽이랑 부디 치는 경우
        if(y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    // 게임 끝나는 메소드
    public void gameOver(Graphics g) {
        // JButton btn = new JButton("restart");

        // score
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + pointEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + pointEaten))/2, g.getFont().getSize());

        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GameOver", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2+20, SCREEN_HEIGHT/2);

        // restart game button
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        // JFrame.add(btn);
        g.drawString("Restart", (SCREEN_WIDTH - metrics.stringWidth("Restart"))/2 + 70, SCREEN_HEIGHT - 25 );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) { // running == true
            move();
            checkPoint();
            checkCollisions();
        }
        repaint();
    }

    // 내부 클래스
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        // 키를 눌렀을때 메소드 호출
        public void keyPressed(KeyEvent e) {
            // snake 움직이기
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
            }
        }
    }
}