import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

public class Interpreter {

    // ���O�Ɍ��߂��Ă��鉉�Z�q�Ȃǂ̌��z��ɂ܂Ƃ߂�
    // �\���z��
    String[] reserved_word = { "input", "print", "end", "print" };
    // ���Z�q�z��
    String[] operator = { "+", "-", "*", "/", "=" };

    /*
     * ���͂��ꂽ�v���O�����̎�ނ��i�[����ϐ� 1: input�� 2: print�� 3: end�� 4: ����� 5:�G���[
     */
    static int process_type = 0;

    public static void main(String[] args) {

        // �����̓ǂݍ��݂̂��߂ɁAScanner�N���X�̃C���X�^���X����
        Scanner scan = new Scanner(System.in);

        while (true) {

            System.out.printf("%s ", ">");

            // ���͂����v���O������String�^��inputProgram�ɓǂݍ���
            String inputProgram = scan.nextLine();

            // �����́{�\�����
            TokenAnalysis(inputProgram);

            System.out.println(process_type);

            // process_type��3��5�Ȃ�AWhile���𔲂��A�v���O�����I��
            if (process_type == 3 || process_type == 5) {
                break;
            }

            // System.out.println(inputProgram);

        }

        // Scanner�N���X�̃C���X�^���X���N���[�Y
        scan.close();

    }

    public static void TokenAnalysis(String text) {

        // ������

        // ���͂���������text��v�f���Ƃɕ���
        List<String> token = Row_split(text);

        String element = token.get(0);

        // �v���O�����̍ŏ��̗v�f��process_type�̐ݒ�
        switch (element) {
        case "input":
            process_type = 1; // input��
            break;
        case "print":
            process_type = 2; // print��
            break;
        case "end":
            process_type = 3; // end��
            break;
        default:
            // �����
            // �����������O���ł��邩�ǂ����m�F
            if (CheakName(element)) {
                process_type = 4;
            } else {
                process_type = 5;
                return;
            }

        }

        // �\�����

        // process_type = 1�̂Ƃ��A����token�������O���ł��邱�Ƃ��m�F
        if (process_type == 1) {
            element = token.get(1);
            if(!CheakName(element)){
                process_type = 5;
                return;
            }
        }

        // process_type = 4�̂Ƃ��A����token��"="�ł��邱�Ƃ��m�F
        if (process_type == 4) {
            element = token.get(1);
            if(!element.equals("=")){
                System.out.println("Error : input���̋L�q�@���Ⴂ�܂��B�����O��=�������̌`�ŋL�q���Ă��������B");
                process_type = 5;
                return;
            }
        }

    }

    /*
     * �֐����FCheakName �����FString text �߂�l�FBoolean
     * �����Ftext�������O���̌`���i�������p�����p�����݂̂ō\������Ă���j�ł���Ȃ��true�A�����łȂ����false��߂�l�Ƃ��ĕԂ�
     */
    static Boolean CheakName(String text) {

        if (Pattern.matches("^[a-zA-Z]+$", text.substring(0, 1))) {
            if(!Pattern.matches("^[0-9a-zA-Z]+$", text)){
                System.out.println("Error : �����O���̌`�����Ⴂ�܂��B�p�����݂̂��g�p���Ă��������B");
                return false;
            }
        } else {
            System.out.println("Error : �����O���̕����ɂ͉p�����g�p���Ă��������B");
            return false;
        }

        return true;

    }

    /*
     * �֐����FRow_split �����FString row �߂�l�FString[]
     * �����Frow��v�f���Ƃɕ������A�v�f���Ƃ�String�^�̔z��Ɋi�[���A�Ԃ�l�Ƃ��ĕԂ��B
     */
    static List<String> Row_split(String row) {

        // row�̒��g��v�f���ƂɊi�[����z��
        List<String> element = new ArrayList<String>();

        // row�̋󔒁A���䕶���̍폜
        row = row.trim();
        // �����̊Ԃ̔��p�X�y�[�X���폜
        // row = row.replaceAll(" ", "");

        // row����v�f�����o���ۂ�row�̍����A�E���̓Y��
        int left = 0, right = 0;

        // row�̒��������J��Ԃ�
        for (int i = 0; i <= row.length(); i++) {
            // row�̍Ō�̕����ɗ�������s
            if (right == row.length()) {
                // cut��row��left�Ԗڂ̕�������right�Ԗڂ̕�����؂�o���đ��
                String cut = row.substring(left, right);
                // cut�̒�����0�łȂ��icut���󕶎��łȂ���΁j���s
                if (cut.length() != 0) {
                    // element��cut����
                    element.add(cut);
                }
            }
            // row��i�Ԗڂ̕����� ' ' �ł���Ύ��s����i�����̕����͖����j
            else if (row.charAt(i) == ' ') {
                String cut = row.substring(left, right);
                if (cut.length() != 0) {
                    element.add(cut);
                }
                // left,right��i+1�ɐݒ�
                left = i + 1;
                right = i + 1;
            }
            // row��i�Ԗڂ̕����� ')' '+' '-' '*' '/'�ł���Ύ��s
            else if ((row.charAt(i) == ' ') || (row.charAt(i) == ')') || (row.charAt(i) == '+')
                    || (row.charAt(i) == '-') || (row.charAt(i) == '*') || (row.charAt(i) == '/')
                    || (row.charAt(i) == '=')) {
                String cut = row.substring(left, right);
                if (cut.length() != 0) {
                    element.add(cut);
                }
                element.add(String.valueOf(row.charAt(i)));
                // left,right��i+1�ɐݒ�
                left = i + 1;
                right = i + 1;
            }
            // row��i�Ԗڂ̕����� '(' �ł���Ύ��s
            else if (row.charAt(i) == '(') {

                element.add(String.valueOf(row.charAt(i)));

                left = i + 1;
                right = i + 1;
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