
import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GameFrame() {
        // GamePanel panel = new GamePanel() 에서 panel 대신에 new GamePanel() 을 넣기
        this.add(new GamePanel());

        // JFrame 관련 메소드들
        this.setTitle("Snake"); // 창의 타이틀
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임의 클로우즈 ( x 버튼 ) 개시했을뗴 디폴트로 실행처리하는 메소드
        // EXIT_ON_CLOSE 는 어플리케이션 종료
        this.setResizable(false); // 창의 크기를 조절할 수 없도록 하기
        this.pack(); // JFrame 의 내용물에 맞게 윈도우 크기 조절
        this.setVisible(true); // 창에 화면이 나타낼 것인지 설정
        this.setLocationRelativeTo(null); // frame 위치를 컴포넌트에 따라 상대적인 위치에 지정. 매개변수 null 을 넣으면 화면의 정중앙에 프레임 위치
    }
}

