// ���K�P�v���O����
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class XdashToken{
    public static void main(String[] args) throws IOException {

        // ���O�Ɍ��߂��Ă��鉉�Z�q�Ȃǂ̌��z��ɂ܂Ƃ߂�
        // �\���z��
        String[] reserved_word = {"program","while","if"};
        // �^���z��
        String[] type = {"int","String","char"};
        // �֐��z��
        String[] function = {"print"};
        // ���Z�q�z��
        String[] operator = {"+","-",":="};
        String[] operater_comp = {"<","<=",">=",">"};
        // �R�����g�z��
        String[] comment = {"//","/*","*/"};

        // �e��ނɕ��ނ��ꂽ�v�f���i�[���郊�X�g
        // ��ނP�i�\���j
        List<String> type1 = new ArrayList<String>();
        // ��ނQ�i���ʎq�F�v���O�������j
        List<String> type2 = new ArrayList<String>();
        // ��ނS�i�^���j
        List<String> type4 = new ArrayList<String>();
        // ��ނU�i�֐����j
        List<String> type6 = new ArrayList<String>();
        // ��ނV�i�萔�j
        List<String> type7 = new ArrayList<String>();
        // ��ނW�i���ʂ̉��Z�q�j
        List<String> type8_nomal = new ArrayList<String>();
        // ��ނW�i��r���Z�q�j
        List<String> type8_comp = new ArrayList<String>();
        // ��ނX�i���ʎq�F�ϐ����j
        List<String> type9 = new ArrayList<String>();

        // �g�[�N���̎�ޔԍ�
        int type_number = 0;
        // �O�̗v�f�̎�ޔԍ�
        int pre_type_number = 0;
        // comment�����̍s�ɑ����Ă��邩�𔻒�
        int comment_flag = 0;
        // ���̍s�Ɏ��4�����邩�ǂ����𔻒�
        int type4_flag = 0;
        // id
        int id;

        // �t�@�C���̓��e��get���A1�s����String�^�̃��X�gcode�ɑ��
        Path path = Paths.get("Sample.txt");
        List<String> code = Files.readAllLines(path);

        // ���͂��ꂽ�v���O�����̍s�������J��Ԃ�
        for(int i=0; i<code.size(); i++){
            // 1�s�������o���B
            String row = code.get(i);
            // �e�v�f�ɕ�������
            List<String> element = Row_split(row);

            // �v�f�̌������J��Ԃ�
            for(int j = 0; j < element.size(); j++){
                // 1�O�̗v�f�̎�ޔԍ����X�V 
                pre_type_number = type_number;

                // ��ނ̔���
                while(true){
                    // �R�����g�̔���(*/)
                    if(element.get(j).equals("*/")){
                        // �R�����g�̏I��
                        comment_flag = 0;
                        break;
                    }

                    // comment_flag��1�ł���Ԃ͔�����s��Ȃ�
                    if(comment_flag == 1){
                        break;
                    }

                    // �R�����g�̔���(/*)
                    if(element.get(j).equals("/*")){
                        // �R�����g�̊J�n
                        comment_flag = 1;
                        break;
                    }

                    // �R�����g�̔��������(//)
                    if(element.get(j).equals("//")){
                        // for���̏I���������w�肵�A���̍s�̈ȍ~�̔�����s��Ȃ��悤�ɂ���
                        j = element.size();
                        break;
                    }

                    // ��ނP(�\���)
                    // ���݂̗v�f��reserved_word�Ɋi�[����Ă��邩���ׂ�
                    id = Judgment(element.get(j), reserved_word, type1);
                    //�@�i�[����Ă���Ƃ����s
                    if(id != -1){
                        // ��ޔԍ��̍X�V
                        type_number = 1;
                        // ���ʂ̏o��
                        Output(type_number, element.get(j), id, 1, type1);
                        // while�����甲����
                        break;
                    }

                    // ��ނS(�^��)
                    id = Judgment(element.get(j), type, type4);
                    if(id != -1){
                        type_number = 4;
                        Output(type_number, element.get(j), id, 1, type4);
                        // type4_flag��1�����i�^�̐錾�����邽�߁j
                        type4_flag = 1;
                        break;
                    }

                    // ��ނU�i�֐����j
                    id = Judgment(element.get(j), function, type6);
                    if(id != -1){
                        type_number = 6;
                        Output(type_number, element.get(j), id, 1, type6);
                        break;
                    }

                    // ��ނW�i���ʂ̉��Z�q�j
                    id = Judgment(element.get(j), operator, type8_nomal);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, element.get(j), id, 1, type8_nomal);
                        break;
                    }

                    // ��ނW�i��r���Z�q�j
                    id = Judgment(element.get(j), operater_comp, type8_comp);
                    if(id != -1){
                        type_number = 8;
                        Output(type_number, element.get(j), id, 11, type8_comp);
                        break;
                    }

                    // ��ނX
                    type_number = 9;
                    // type9�Ɍ��݂̗v�f���i�[����Ă��邩���ׂ�
                    id = Search(element.get(j), type9);
                    // �i�[����Ă����Ƃ��Ɏ��s
                    if(id != -2){
                        // ���ʂ��o��
                        System.out.println("(" + type_number + "," + (id+1) + ")");
                        break;
                    }
                    // type9�Ɍ��݂̗v�f���i�[����Ă��Ȃ��Ƃ�
                    else{
                        // ���̍s��type4�̗v�f���������ꍇ�i�^�����錾����Ă����ꍇ�j
                        if(type4_flag == 1){
                            // type9�Ɍ��݂̗v�f��ǉ�����
                            type9.add(element.get(j));
                            // ���ʂ��o��
                            System.out.println("(" + type_number + "," + type9.size() + ")");
                            break;
                        }
                        
                    }

                    // ��ނQ
                    if(pre_type_number == 1){
                        // ���݂̗v�f��type9�Ő錾����Ă���ϐ����ł͂Ȃ�������type2�Ɣ��f����
                        id = Search(element.get(j), type9);
                        if(id == -2){
                            type_number = 2;
                            id = Search(element.get(j), type2);
                            Output(type_number, element.get(j), id, 1, type2);
                            break;
                        }
                    }
                    
                    // ��ނV
                    if(pre_type_number == 8 || pre_type_number == 6 ){
                        type_number = 7;
                        id = Search(element.get(j), type7);
                        if(id != -2){
                            System.out.println("(" + type_number + "," + element.get(j) + ")");
                            break;
                        }
                        else{
                            type7.add(element.get(j));
                            System.out.println("(" + type_number + "," + element.get(j) + ")");
                            break;
                        }
                    }
                }    
            }
            // �s���ς�邽�߁Atype4_flag��0�ɖ߂�
            type4_flag = 0;
        }   
    }

    /*
    �֐����FOutput
    �����Fint type_number, String element, int id, int pading, List<String> type
    �߂�l�F�Ȃ�
    �����F�R���\�[���Ɍ`���ɏ]���āA���ʂ��o�͂�����B
          ���8�ł́A��ނ��Ƃ�id�ԍ��ɓ���������̂ŁApading�ł����ݒ肷��
    �@�@�@ elememt��type�ɒǉ�����Ă��Ȃ���΁A�o�^����B
    */
    static void Output(int type_number, String element, int id, int pading, List<String> type){

        if(id != -2){
            // ���ʂ��o��
            System.out.println("(" + type_number + "," + (id+pading) + ")");
        }
        else{
            // type��element��ǉ�
            type.add(element);
            // ���ʂ��o��
            System.out.println("(" + type_number + "," + (type.size()+pading-1) + ")");
        }
    }

    /*
    �֐����FSerch
    �����FString target List<String> type
    �߂�l�Fint
    �����Ftarget��type�Ɋi�[����Ă��Ȃ����ǂ����𒲂ׂ�B
         �i�[����Ă���΁A���̓Y����Ԃ��B�����łȂ����-2��Ԃ��B
    */
    static int Search(String target, List<String> type){

        // search�Ɂ\2�����i�i�[����Ă��Ȃ��Ɖ���j
        int search = -2;

        // type�̃T�C�Y�����J��Ԃ�
        for(int i=0; i < type.size(); i++){
            // target��type��i�Ԗڂ̗v�f�Ɠ������Ȃ�Ύ��s
            if(target.equals(type.get(i))){
                // search��i�ɂ���
                search = i;
                // judgement��Ԃ�
                return search;
            }
        }
        return search;
    }
    
    /*
    �֐����FJudgment
    �����FString target, String[] judge,List<String> type
    �߂�l�Fint
    �����Ftarget��judge�z��̃f�[�^���i�[����Ă��Ȃ����ǂ������ׂ�B
    �@�@�@�����A�i�[����Ă��Ȃ���΁A-1��Ԃ�l�Ƃ��ĕԂ��B
         �i�[����Ă��āA���ꂪ���ł�type�ɓo�^����Ă���΁A���̓Y����Ԃ��B
         �����łȂ���΁A-2��Ԃ�l�Ƃ��ĕԂ��B
    */
    static int Judgment(String target, String[] judge, List<String> type){

        // judment�Ɂ\1��������i�i�[����Ă��Ȃ��Ɖ��肷��j
        int judgement = -1;

        // judge�z��̒��������J��Ԃ�
        for(int i=0; i<judge.length; i++){

            // judge[i]��target����������Ύ��s
            if(target.equals(judge[i])){
                // type�̃T�C�Y�����J��Ԃ�
                for(int k=0; k < type.size(); k++){
                    // �����Atype��target���i�[����Ă���Ύ��s
                    if(target.equals(type.get(k))){
                        // judgement��Y���ɂ���
                        judgement = k;
                        // judgement��Ԃ�
                        return judgement;
                    }
                }
                judgement = -2;
                return judgement;
            }
        }
        return judgement;
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
            // row��i�Ԗڂ̕����� ' '�@','�@';' ')'�@'}'�@�ł���Ύ��s����i�����̕����͖����j
            else if( (row.charAt(i) == ' ') || (row.charAt(i) == ',')  || (row.charAt(i) == ';') || (row.charAt(i) == ')')  || (row.charAt(i) == '}') ){
                String cut = row.substring(left, right);
                if(cut.length() != 0){
                    element.add(cut);
                }
                // left,right��i+1�ɐݒ�
                left = i+1;
                right = i+1;
            }
            // row��i�Ԗڂ̕����� '(' '{'�@�ł���Ύ��s
            else if ( (row.charAt(i) == '(') ||  (row.charAt(i) == '{') ){
                left = i+1;
                right = i+1;
            }
            else {
                // right��1���₷
                right++;
            }
        }
        // element��Ԃ�
        return element;
    }
}
