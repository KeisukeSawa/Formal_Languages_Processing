import java.util.Scanner;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

class Calculator{

    // Loolahead()�ŗ��p����ϐ�
    // token��ۑ�����ϐ�
    static String token;
    // �g�[�N�����X�g���玟�̃g�[�N�������o�����߂�index
    static int index = 2;
    // �g�[�N�����i�[���郊�X�g
    static List<String> element = new ArrayList<String>();


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

        // 



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
         
         /*
        // �v�f�̌������J��Ԃ�
        for(int i = 0; i < element.size(); i++){
            System.out.println(element.get(i));
        }
        */

        // 1�Ԗڂ�token��token�ɑ��
        Loolahead();


        



    }

    /* 
     �֐����FLoolahead
     �����Fstatic�t�B�[���h�ϐ��ł���token�Ƀg�[�N�����X�gelement���玟�̃g�[�N����������
     */
    static void Loolahead(){

        // ���̃g�[�N������
        token = element.get(index);

        // index��1���₷
        index++;
    }

    static void Expression(){

    }

    static void Term(){

    }

    static void Factor(){

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