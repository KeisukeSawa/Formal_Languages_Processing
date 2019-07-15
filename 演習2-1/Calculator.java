import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

class Calculator{

    // Loolahead()�ŗ��p����ϐ�
    // token��ۑ�����ϐ�
    static String token;
    // �g�[�N�����X�g���玟�̃g�[�N�������o�����߂�index
    static int index = 2;
    // �g�[�N�����i�[���郊�X�g
    static List<String> element = new ArrayList<String>();

    // �t�|�[�����h�̏o�͂��i�[���郊�X�g
    static List<String> reversePolandResult = new ArrayList<String>();

    public static void main(String[] args){

        // �����̓ǂݍ��݂̂��߂ɁAScanner�N���X�̃C���X�^���X����
        Scanner scan = new Scanner(System.in);

        // ���b�Z�[�W�̏o��
        System.out.println("������͂��Ă�������");
        System.out.println("���͌`���F'�ϐ���' = '��' �@���̌`���łȂ���Ύ󗝂��܂���B" );
        System.out.println("���͗�Fresult = (987-(654/327)*123)+456");

        // ���͂�������String�^��formula�ɓǂݍ���
        String formula = scan.nextLine();

        // Scanner�N���X�̃C���X�^���X���N���[�Y
        scan.close();

        // formula���t�|�[�����h�L�@�ɕϊ����A�t�@�C���o��
        ReversePoland(formula);

        // ���̓��������߂�
        Solve();

    }

    /*
        �֐����FSolve
        �����FList<String> reversePolandResult
        �����F�t�|�[�����h�`���ɕϊ������������ۂɌv�Z���A���������ߏo�͂���
     */
    static void Solve(){

        // �X�^�b�N���쐬����iDeque�𗘗p�j
        Deque<Integer> stack = new ArrayDeque<Integer>();

        int a,b;

        for(int i=0; i<reversePolandResult.size(); i++){

            switch (reversePolandResult.get(i)) {
                case "+":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b+a);
                    break;
                case "-":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b-a);
                    break;
                case "*":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b*a);
                    break;
                case "/":
                    a = stack.pop();
                    b = stack.pop();
                    stack.push(b/a);
                    break;
                default:
                    stack.push(Integer.parseInt(reversePolandResult.get(i)));
            }
        }

        System.out.printf("%s %s %d\n",element.get(0),element.get(1),stack.pop());

    }

    /* 
       �֐����FReversePoland
     �@�����FString formula
       �����F�^����ꂽ��formula���t�|�[�����h�L�@�ɕϊ����AreversePoland.txt�ɂ��̌��ʂ��o�͂���
     */
    static void ReversePoland(String formula){

        element = Row_split(formula);
        
        /* element�̍ŏ���2�v�f�́A'������i�ϐ����j','='�ł���Ɖ���
           element�̍ŏ���2�v�f�ɂ��ẮA�t�|�[�����h�̑ΏۂƂ��Ȃ��B
         */ 

        // ���߂�token��token�ɑ��
        Loolahead();

        // �\����͊J�n
        Expression();

        // reversePoland.txt�Ɍ��ʂ��o��
        try{
            File file = new File("reversePoland.txt");
            FileWriter filewriter = new FileWriter(file);

            for(int i=0; i<reversePolandResult.size();i++){
                
              filewriter.write(reversePolandResult.get(i));
              filewriter.write(" ");

            }
            
            filewriter.close();

        }catch(IOException e){
            System.out.println(e);
        }
        
    }

    /* 
     �֐����FLoolahead
     �����Fstatic�t�B�[���h�ϐ��ł���token�Ƀg�[�N�����X�gelement���玟�̃g�[�N����������
     */
    static void Loolahead(){

        if(index == element.size()){
            token = "end";
        }
        else{
            // ���̃g�[�N������
            token = element.get(index);

            // index��1���₷
            index++;
        }
    }

    /*
      �֐����FExpression
    �@�����F���@Expression������
            Expression -> term (patern 1)
            Expression -> term '+' term (patern 2)
            Expression -> term '-' term (patern 3)
     */
    static void Expression(){

        // term�����s
        Term();

        while(true){

            // �o�͑Ώۂ̃g�[�N����output_expression�ɑ��
            String output_expression = token;

            // token��'+'�����'-'�Ȃ���s
            if(token.equals("+") || token.equals("-")){

                // ���̃g�[�N����token�ɑ��
                Loolahead();
                // Term�����s
                Term();

                reversePolandResult.add(output_expression);
                System.out.println(output_expression);

            }
            else{
                return;
            }

        }

    }

    /*
      �֐����FTerm
    �@�����F���@term������
            term -> factor (patern 1)
            term -> factor '*' factor (patern 2)
            term -> factor '/' factor (patern 3)
     */
    static void Term(){

        // Factor�����s
        Factor();

        while(true){

            // �o�͑Ώۂ̃g�[�N����output_term�ɑ��
            String output_term = token;


            // token��'*'�����'/'�Ȃ���s
            if(token.equals("*") || token.equals("/")){

                // ���̃g�[�N����token�ɑ��
                Loolahead();
                // Term�����s
                Factor();

                reversePolandResult.add(output_term);
                System.out.println(output_term);

            }
            else{
                return;
            }

        }
        
        
    }

    /*
      �֐����FFactor
    �@�����F���@factor������
            factor -> const(�萔) (patern 1)
            factor -> '(' expression ')' (patern 2)
     */
    static void Factor(){

        // �o�͑Ώۂ̃g�[�N����output_term�ɑ��
        String output_factor = token;

        // token��'*'�����'/'�Ȃ���s
        if(token.equals("(")){

            // ���̃g�[�N����token�ɑ��
            Loolahead();

            // Expression�����s
            Expression();

            if(!token.equals(")")){
                System.out.println(" ')' missing");
            }

            // ���̃g�[�N����token�ɑ��
            Loolahead();

        }
        else{

            // CONST�ł���Ɣ��f
            reversePolandResult.add(output_factor);
            System.out.println(output_factor);

            // ���̃g�[�N����token�ɑ��
            Loolahead();

        }
    }

    /* 
    �֐����FRow_split
    �����FString row
    �߂�l�FString[]
    �����Frow��v�f���Ƃɕ������A�v�f���Ƃ�String�^�̔z��Ɋi�[���A�Ԃ�l�Ƃ��ĕԂ��B
         ���̍ہA��؂蕶���̃R���}, �Z�~�R����; �u���b�N��\�� curly blacket�@( {�@�� } )����� ���� ( ) �͓ǂݔ�΂��B�@
    */
    static List<String> Row_split(String row){

        // row�̒��g��v�f���ƂɊi�[����z��
        List<String> element = new ArrayList<String>();

        // row�̋󔒁A���䕶���̍폜
        row = row.trim();
        // �����̊Ԃ̔��p�X�y�[�X���폜
        row = row.replaceAll(" ", "");

        // row����v�f�����o���ۂ�row�̍����A�E���̓Y��
        int left = 0, right = 0;

        // row�̒��������J��Ԃ�
        for(int i=0; i<=row.length(); i++){
            // row�̍Ō�̕����ɗ�������s
            if(right == row.length() ){
                // cut��row��left�Ԗڂ̕�������right�Ԗڂ̕�����؂�o���đ��
                String cut = row.substring(left, right);
                // cut�̒�����0�łȂ��icut���󕶎��łȂ���΁j���s
                if(cut.length() != 0){
                    // element��cut����
                    element.add(cut);
                }
            }
            // row��i�Ԗڂ̕����� ')' '+' '-' '*' '/'�ł���Ύ��s
            else if( (row.charAt(i) == ')') || (row.charAt(i) == '+') || (row.charAt(i) == '-') || (row.charAt(i) == '*') || (row.charAt(i) == '/') || (row.charAt(i) == '=') ){
                String cut = row.substring(left, right);
                if(cut.length() != 0){
                    element.add(cut);
                }
                element.add(String.valueOf(row.charAt(i)));
                // left,right��i+1�ɐݒ�
                left = i+1;
                right = i+1;
            }
            // row��i�Ԗڂ̕����� '(' �ł���Ύ��s
            else if ( row.charAt(i) == '(' ){

                element.add(String.valueOf(row.charAt(i)));

                left = i+1;
                right = i+1;
            }
            // ���̑��i�����A�����j�ł���Ύ��s
            else {
                // right��1���₷
                right++;
            }
        }
        // element��Ԃ�
        return element;
    }

}