package Solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * 영어 끝말잇기
 * https://school.programmers.co.kr/courses/10586/lessons/67680
 */
public class EnglishWordChain {

    /**
     * [탈락의 경우]
     * 1. 끝말잇기의 규칙에 어긋나는 경우 (끝 문자로 시작하는 단어를 말하지 않은 경우)
     * 2. 중복되는 단어를 말한 경우
     *
     * [구현 방법 구상]
     * 1. Player 라는 객체를 가진다.
     * - 가지고 있는 단어 목록과, 순서, player number 정보를 갖는다.
     * 3. 끝말잇기 게임 객체도 하나 가진다.
     * - 단어가 적합한지 여부를 판단한다.
     * - 이를 위해 끝글자 및 history도 관리해야 한다.
     */
    public int[] solution(int n, String[] words) {
        WordChainGame game = new WordChainGame(n, words);

        return game.play();
    }

    //TODO: refactoring 하기 !
    // 0. 테스트 코드 통과 여부 파악
    // 1. 지역 변수들에 대해 접근자, static 여부 고민하고 설정해보기
    // 2. Players 객체 생성
    // 3. 보다 효율적으로 풀어낼 수 있는 로직 수정 (다른 사람의 로직 참고)
    // 4. Stream 사용 가능한 부분 사용
    // 5. 불 필요한 지역변수 및 소스코드는 없는지.. 여부 판단 하여 삭제
    // 6. 주석 처리된 테스트코드를 먼저 보고, 삭제 하지 않고 유지할 수 있는 방법 없는 지 고민하고 Refactoring 하기

    public class Player {
        private int number;         // Player Number (start with 1)
        private String[] words;     // 가진 단어들
        private int order;          // 단어 제시 순서

        public Player(int number, String[] words) {
            this.number = number;
            this.words = words;
            this.order = 0;
        }

        public String sayWord() {
            order++;
            return words[order - 1];
        }

        public String[] getWords() {
            return words;
        }

        public int getOrder() {
            return this.order;
        }

        public int getNumber() {
            return this.number;
        }
    }

    public class WordChainGame {
        private int playerCount;        // 게임 참여 Player 수
        private String[] words;         // 전체 단어
        private List<Player> players;
        private List<String> wordHistory = new ArrayList<>();   // 제시한 단어 History;

        public WordChainGame(int playerCount, String[] words) {
            this.playerCount = playerCount;
            this.words = words;
            this.players = assignPlayers();
        }

        private List<Player> assignPlayers() {
            List<Player> players = new ArrayList<>();

            for(int i = 1; i <= playerCount; i++) {
                players.add(new Player(i, pickPlayerWords(this.playerCount, i, this.words)));
            }

            return players;
        }

        private String[] pickPlayerWords(int playerCount, int playerNumber, String[] entireWords) {
            List<String> words = new ArrayList<>();

            // TODO: 더 효율적으로 바꿀 수 있을 것 같다. (Stream을 이용할 수 있을 것 같다.)
            // TODO: While을 이용해도 될 거 같다.
            for(int i = 0; i < entireWords.length; i++) {
                if(playerCount == playerNumber) {
                    if((i+1) % playerCount == 0) {
                        words.add(entireWords[i]);
                    }
                }
                else if((i+1) % playerCount == playerNumber) {
                    words.add(entireWords[i]);
                }
            }

            return words.stream()
                    .toArray(String[]::new);
        }

        public int[] play() {
            int[] result = {0, 0};

            while(wordHistory.size() <= words.length) {
                // TODO: Players가 한번씩 돌아가면서 얘기한다. - 객체로 분리된다면, Players 로 책임을 넘긴다.
                for(Player player : players) {
                    if(!addWordHistory(player.sayWord())) {
                        result[0] = player.getNumber();
                        result[1] = player.getOrder();

                        return result;
                    }

                    if(wordHistory.size() == words.length) {
                        return result;
                    }
                }
            }

            return result;
        }

        public boolean addWordHistory(String word) {
            if(isCorrectWord(word)) {
                wordHistory.add(word);
                return true;
            }
            return false;
        }

        private boolean isCorrectWord(String word) {
            // 1. 끝말잇기 룰에 부합하는 지 검사
            if(wordHistory.size() < 1) {
                return true;
            }

            String tailCharacter = getTailCharacter(wordHistory);
            if(!tailCharacter.equals(word.substring(0, 1))) {
                return false;
            }

            // 2. 중복되는 단어가 불려졌는 지 검사
            if(wordHistory.contains(word)) {
                return false;
            }

            return true;
        }

        private String getTailCharacter(List<String> wordHistory) {
            String lastWord = wordHistory.get(wordHistory.size() - 1);

            return lastWord.substring(lastWord.length() - 1);
        }

        public List<Player> getPlayers() {
            return players;
        }
    }
}
