class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        return Solution1.topKFrequent(nums, k);
    }

    static class Solution1 {
        /**
         * TimeComplexity:  O(N*log(N))
         * SpaceComplexity: O(N)
         *
         * Where:
         *      N := nums.size()
         */

        static int[] topKFrequent(int[] nums, int K) {
            // Create a frequency map.
            Map<Integer, Integer> freqMap = new HashMap<>();
            Arrays.stream(nums).forEach(
                    num -> {
                        freqMap.putIfAbsent(num, 0);
                        freqMap.replace(num, freqMap.get(num) + 1);
                    });
            // Create a list of Node{val, freq} and sort it according to the frequency.
            List<Node> nodes = freqMap.entrySet().stream()
                .map(e -> Node.create(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
            Collections.sort(nodes, (n1, n2) -> -Integer.compare(n1.getFreq(), n2.getFreq()));
            // Grab first K elements and return them.
            int[] topK = new int[K];
            for (int i = 0; i < K; i++) {
                topK[i] = nodes.get(i).getVal();
            }

            return topK;
        }

        static class Node {
            int val;
            int freq;

            int getVal() {
                return val;
            }

            int getFreq() {
                return freq;
            }

            Node(int val, int freq) {
                this.val = val;
                this.freq = freq;
            }

            static Node create(int val, int freq) {
                return new Node(val, freq);
            }
        }
    }

    static class Solution2 {
        /**
         * TimeComplexity:  O(?)
         * SpaceComplexity: O(?)
         */

        // Can we do better than O(N * log(N)) ?
        // What if we maintained a frequency stack?
        // |
        // *--> What is a "frequency stack"?
        //      This would be a data structure that keeps the
        //      most frequent objects (appearing from a running
        //      stream) at the top of the stack.
        // |
        // *--> What is the point of this stack?
        //      We could turn our numbers into this stack and at
        //      the end of the numbers, we can grab the first K
        //      elements.
        // |
        // *--> How would you design such a stack?
        //      The stack would be represented by a linked list
        //      of Node{val, freq}.
        //      If node `v` comes before node `u`, then it means
        //      that `v.freq >= u.freq`.
        //      When adding a new node, put it in the back of the
        //      linked-list and keep track of it in a Map<int, Node>.
        //      Initially, new nodes will have frequency of 1.
        static int[] topKFrequent(int[] nums, int k) {
        }

        static class Node {
            int val;
            int freq;
            Node prev;
            Node next;

            int getVal() {return val;}
            int getFreq() {return freq;}
            int getPrev() {return prev;}
            int getNext() {return next;}
            void increment() {freq++;}
            boolean canPromote() {return prev != null && prev.getFreq() < this.getFreq();}
            void setPrev(Node prev) {this.prev = prev;}
            void setNext(Node next) {this.next = next;}
            void tryPromote() {
                if (!canPromote()) {
                    return;
                }
                Node oldPrev = prev;
                oldPrev.setNext(this.getNext());
                setPrev(prev.getPrev());
                setNext(oldPrev);   
            }
            Node(int val) {
                this.val = val;
                this.freq = 0;
            }
            static Node create(int v) {return new Node(v);}
        }

        static class FrequencyStack {
            Node head;
            Node tail;
            Map<Integer, Node> nodeMap = new HashMap<>();

            void increment(int v) {
                if (!nodeMap.contains(v)) {
                    // New node.
                    addToTail(v);
                    return;
                }
            }

            void addToTail(int v) {
                Node newTail = Node.create(v);
                if (tail == null) {
                    head = newTail;
                    tail = newTail;
                    nodeMap.put(v, newTail);
                    return;
                }

            }
        }
    }
}
