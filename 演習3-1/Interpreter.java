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

    // �����̓ǂݍ��݂̂��߂ɁAScanner�N���X�̃C���X�^���X����
    public static Scanner scan = new Scanner(System.in);

    // ���O���i�[���郊�X�g
    static List<String> name_list = new ArrayList<String>();

    // ���O�ɕR�Â����Ă���l���i�[���郊�X�g
    static List<String> name_value_list = new ArrayList<String>();

    // ���̃g�[�N�����i�[���郊�X�g
    static List<String> term_token = new ArrayList<String>();

    // �t�|�[�����h�̏o�͂��i�[���郊�X�g
    static List<String> reversePolandResult = new ArrayList<String>();

    // token��ۑ�����ϐ�
    static String token;

    // �g�[�N�����X�g���玟�̃g�[�N�������o�����߂�index
    static int index = 2;

    // <��>�̌v�Z���ʂ�ێ�����ϐ�
    static int formula_result;

    public static void main(String[] args) {

        while (true) {
            System.out.printf("%s ", ">");
            // ���͂����v���O������String�^��inputProgram�ɓǂݍ���
            String inputProgram = scan.nextLine();
            // �����́{�\�����
            TokenAnalysis(inputProgram);
            // process_type��3��5�Ȃ�AWhile���𔲂��A�v���O�����I��
            if (process_type == 3 || process_type == 5) {
                break;
            }
        }

        // Scanner�N���X�̃C���X�^���X���N���[�Y
        scan.close();
    }

    public static void TokenAnalysis(String text) {

        // ������
        // ���͂���������text��v�f���Ƃɕ���
        List<String> token = Row_split(text);

        // �v���O�����̍ŏ��̗v�f���i�[����ϐ�element
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
        // ���̌�Ainput�������s�����߂ɁAInput()�����s
        if (process_type == 1) {
            element = token.get(1);
            if (!CheakName(element)) {
                process_type = 5;
                return;
            }
            Input(element);
        }

        // process_type = 4�̂Ƃ��A����token��"="�ł��邱�Ƃ��m�F
        // ���̌�A������̏������s�����߂�Assignment()�����s
        if (process_type == 4) {
            element = token.get(1);
            if (!element.equals("=")) {
                System.out.println("Error : input���̋L�q�@���Ⴂ�܂��B�����O��=�������̌`�ŋL�q���Ă��������B");
                process_type = 5;
                return;
            }
            Assignment(token);
        }

        // process_type = 2�̂Ƃ��Aprint���̏������s�����߂�Print()�����s
        if (process_type == 2) {
            Print(token);
        }
    }

    /*
     * �֐����FInput �����FString name �߂�l�F�Ȃ�
     * �����Finput�������s�����\�b�h�A�L�[�{�[�h���͂�҂��A���͂��ꂽ���𖼑Oname�Ɋ���t����
     */
    static void Input(String name) {

        // ���͂����v���O������String�^��inputProgram�ɓǂݍ���
        String value = scan.nextLine();

        // ���͂��ꂽ�����������ł��邩�ǂ����𔻒�
        if (!Pattern.matches("^[0-9]+$", value)) {
            System.out.println("Error : ��������͂��Ă��������B");
            process_type = 5;
            return;
        } else {
            int index = CheakName_list(name);
            // name����`����Ă��Ȃ������ꍇ
            if (index == -1) {
                // ���ꂼ��u���O���X�g�v�Ɓu�l���X�g�v�ɒl����
                name_list.add(name);
                name_value_list.add(value);
            } else {
                // �u�l���X�g�v�ɒl����
                name_value_list.set(index, value);
            }
        }
    }

    /*
     * �֐����FAssignment �����FList<String> �߂�l�F�Ȃ�
     * �����F������������s�����\�b�h�A�E�ӂ́�������]�����Ēl�����߁A���߂��l�����ӂ́����O���Ɋ���t����
     */
    static void Assignment(List<String> program) {

        index = 2;

        // <���O> = <����>�̏ꍇ
        if (program.size() == 3) {
            int index = CheakName_list(program.get(0));
            // name����`����Ă��Ȃ������ꍇ
            if (index == -1) {
                // ���ꂼ��u���O���X�g�v�Ɓu�l���X�g�v�ɒl����
                name_list.add(program.get(0));
                name_value_list.add(program.get(2));
            } else {
                // �u�l���X�g�v�ɒl����
                name_value_list.set(index, program.get(2));
            }
        } else {

            // formula���t�|�[�����h�L�@�ɕϊ����A�t�@�C���o��
            ReversePoland(program);
            // ���̓��������߂�
            Solve();
            int index = CheakName_list(program.get(0));
            // name����`����Ă��Ȃ������ꍇ
            if (index == -1) {
                // ���ꂼ��u���O���X�g�v�Ɓu�l���X�g�v�ɒl����
                name_list.add(program.get(0));
                name_value_list.add(String.valueOf(formula_result));
            } else {
                // �u�l���X�g�v�ɒl����
                name_value_list.set(index, String.valueOf(formula_result));
            }
        }
    }

    /*
     * �֐����FPrint �����FList<String> �߂�l �Ȃ� 
     * �����Fprint���̏������s�����\�b�h ���������v�Z���Č��ʂ��R���\�[���ɏo�͂���
     */
    static void Print(List<String> program) {

        index = 1;

        // formula���t�|�[�����h�L�@�ɕϊ����A�t�@�C���o��
        ReversePoland(program);
        // ���̓��������߂�
        Solve();
        // ���ʂ��o��
        System.out.println(formula_result);

    }

    /*
     * �֐����FCheakName_list ����:String name �߂�l int
     * �����FName_list�̒��ɂ��ł�name�ŗ^���������O������`����Ă��邩���ׂ�B��`����Ă����炻��index�A��`����Ă��Ȃ����-1��Ԃ�
     */
    static int CheakName_list(String name) {

        // name�����ł�name_list�ɑ��݂��Ă��邩���ׁA����index��Ԃ��B
        for (int i = 0; i < name_list.size(); i++) {
            if (name.equals(name_list.get(i))) {
                return i;
            }
        }

        return -1;
    }

    /*
     * �֐����FCheakName �����FString text �߂�l�FBoolean
     * �����Ftext�������O���̌`���i�������p�����p�����݂̂ō\������Ă���j�ł���Ȃ��true�A�����łȂ����false��߂�l�Ƃ��ĕԂ�
     */
    static Boolean CheakName(String text) {

        if (Pattern.matches("^[a-zA-Z]+$", text.substring(0, 1))) {
            if (!Pattern.matches("^[0-9a-zA-Z]+$", text)) {
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
     * �֐����FNameToValue() �����FString<list> �߂�l �Ȃ�
     * �����F�������̒��Ɂ����O�����������ꍇ�Ɂ����O�������ۂ̐��l�ɕϊ����郁�\�b�h
     */
    static void NameToValue(List<String> program) {

        // ���̒��Ɂ����O�������邩�𔻒�
        for (int i = index; i < program.size(); i++) {
            if (!Pattern.matches("^[0-9]+$", program.get(i))) {
                if (!(program.get(i).equals("+") || program.get(i).equals("-") || program.get(i).equals("/")
                        || program.get(i).equals("*") || program.get(i).equals("(") || program.get(i).equals(")"))) {
                    int name_index = CheakName_list(program.get(i));
                    if (name_index == -1) {
                        System.out.println("Error : �������ɂ͊��ɒ�`���Ă��遃���O������͂��Ă��������B");
                        process_type = 5;
                        return;
                    } else {
                        term_token.set(i, String.valueOf(name_value_list.get(name_index)));
                    }
                }
            }
        }
    }

    /*
     * �֐����FSolve �����FList<String> reversePolandResult
     * �����F�t�|�[�����h�`���ɕϊ������������ۂɌv�Z���A���������ߏo�͂���
     */
    static void Solve() {

        // �X�^�b�N���쐬����iDeque�𗘗p�j
        Deque<Integer> stack = new ArrayDeque<Integer>();

        int a, b;

        for (int i = 0; i < reversePolandResult.size(); i++) {

            switch (reversePolandResult.get(i)) {
            case "+":
                a = stack.pop();
                b = stack.pop();
                stack.push(b + a);
                break;
            case "-":
                a = stack.pop();
                b = stack.pop();
                stack.push(b - a);
                break;
            case "*":
                a = stack.pop();
                b = stack.pop();
                stack.push(b * a);
                break;
            case "/":
                a = stack.pop();
                b = stack.pop();
                stack.push(b / a);
                break;
            default:
                stack.push(Integer.parseInt(reversePolandResult.get(i)));
            }
        }

        formula_result = stack.pop();

    }

    /*
     * �֐����FReversePoland �����FString formula
     * �����F�^����ꂽ��formula���t�|�[�����h�L�@�ɕϊ����AreversePoland.txt�ɂ��̌��ʂ��o�͂���
     */
    static void ReversePoland(List<String> formula) {

        term_token = formula;
        // term_token�ɓ����������O���𐔒l�ɕϊ�����
        NameToValue(term_token);

        // ���߂�token��token�ɑ��
        Loolahead();

        // �\����͊J�n
        Expression();

        // reversePoland.txt�Ɍ��ʂ��o��
        try {
            File file = new File("reversePoland.txt");
            FileWriter filewriter = new FileWriter(file);

            for (int i = 0; i < reversePolandResult.size(); i++) {

                filewriter.write(reversePolandResult.get(i));
                filewriter.write(" ");

            }

            filewriter.close();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    /*
     * �֐����FLoolahead 
     * �����Fstatic�t�B�[���h�ϐ��ł���token�Ƀg�[�N�����X�gterm_token���玟�̃g�[�N����������
     */
    static void Loolahead() {

        if (index == term_token.size()) {
            token = "end";
        } else {
            // ���̃g�[�N������
            token = term_token.get(index);

            // index��1���₷
            index++;
        }
    }

    /*
     * �֐����FExpression 
     * �����F���@Expression������
     *  Expression -> term (patern 1)
     *  Expression -> term '+' term (patern 2) 
     *  Expression -> term '-' term (patern 3)
     */
    static void Expression() {

        // term�����s
        Term();

        while (true) {

            // �o�͑Ώۂ̃g�[�N����output_expression�ɑ��
            String output_expression = token;

            switch (token) {
            case "+":
                // ���̃g�[�N����token�ɑ��
                Loolahead();
                // Term�����s
                Term();
                reversePolandResult.add(output_expression);
                break;
            case "-":
                // ���̃g�[�N����token�ɑ��
                Loolahead();
                // Term�����s
                Term();
                reversePolandResult.add(output_expression);
                break;
            default:
                return;
            }
        }

    }

    /*
     * �֐����FTerm
     * �����F���@term������
     * term -> factor (patern 1) 
     * term -> factor '*' factor(patern 2)
     * term -> factor '/' factor (patern 3)
     */
    static void Term() {

        // Factor�����s
        Factor();

        while (true) {

            // �o�͑Ώۂ̃g�[�N����output_term�ɑ��
            String output_term = token;

            switch (token) {
            case "*":
                // ���̃g�[�N����token�ɑ��
                Loolahead();
                // Term�����s
                Factor();
                reversePolandResult.add(output_term);
                break;
            case "/":
                // ���̃g�[�N����token�ɑ��
                Loolahead();
                // Term�����s
                Factor();
                reversePolandResult.add(output_term);
                break;
            default:
                return;
            }
        }
    }

    /*
     * �֐����FFactor
     * �����F���@factor������ 
     * factor -> const(�萔) (patern 1) 
     * factor ->'('expression ')' (patern 2)
     */
    static void Factor() {

        // �o�͑Ώۂ̃g�[�N����output_term�ɑ��
        String output_factor = token;

        switch (token) {
        case "(":
            // ���̃g�[�N����token�ɑ��
            Loolahead();
            // Expression�����s
            Expression();
            if (!token.equals(")")) {
                System.out.println("Error�F')' missing");
            }
            // ���̃g�[�N����token�ɑ��
            Loolahead();
            break;
        default:
            // CONST�ł���Ɣ��f
            reversePolandResult.add(output_factor);
            // ���̃g�[�N����token�ɑ��
            Loolahead();
        }
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