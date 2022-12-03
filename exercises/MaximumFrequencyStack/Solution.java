class FreqStack {

    NodeManager nodeManager;

    public FreqStack() {
        nodeManager = new NodeManager();
    }
    
    public void push(int val) {
        nodeManager.push(val);
    }
    
    public int pop() {
        return nodeManager.pop();
    }


    static class NodeManager {
        private int maxFrequency;
        private int pushTime;
        Map<Integer, Node> values;
        Map<Integer, TreeSet<Node>> frequencyBuckets;

        NodeManager() {
            maxFrequency = 0;
            pushTime = 0;
            values = new HashMap<>();
            frequencyBuckets = new HashMap<>();
        }

        int pop() {
            Node node = peekMaxFrequencyBucket();

            removeNodeFromFrequencyBucket(node, node.getFreq());
            node.pop();
            if (node.getFreq() == 0) {
                values.remove(node.getValue());
            } else {
                addNodeToFrequencyBucket(node, node.getFreq());
            }
            maybeUpdateFrequencyDecrement(node.getFreq());

            return node.getValue();
        }

        void push(int value) {
            if (!values.containsKey(value)) {
                insertNewValue(value);
                return;
            }

            Node node = values.get(value);
            removeNodeFromFrequencyBucket(node, node.getFreq());
            node.push(++pushTime);
            addNodeToFrequencyBucket(node, node.getFreq());
            maybeUpdateFrequencyIncrement(node.getFreq());
        }

        void insertNewValue(int value) {
            Node newNode = Node.create(value, ++pushTime);
            values.put(value, newNode);
            addNodeToFrequencyBucket(newNode, /* frequency= */ 1);
            maybeUpdateFrequencyIncrement(/* frequency= */ 1);
        }

        void addNodeToFrequencyBucket(Node node, int frequency) {
            if (!frequencyBuckets.containsKey(frequency)) {
                frequencyBuckets.put(
                        frequency,
                        new TreeSet<>((a, b) -> Integer.compare(a.peek(), b.peek())));
            }

            frequencyBuckets.get(frequency).add(node);
        }

        void removeNodeFromFrequencyBucket(Node node, int frequency) {
            frequencyBuckets.get(frequency).remove(node);
        }

        void maybeUpdateFrequencyIncrement(int frequency) {
            maxFrequency = Math.max(maxFrequency, frequency);
        }

        void maybeUpdateFrequencyDecrement(int frequency) {
            if (maxFrequency == (frequency+1)
                    && frequencyBuckets.get(maxFrequency).isEmpty()) {
                maxFrequency--;
            }
        }

        Node peekMaxFrequencyBucket() {
            return frequencyBuckets.get(maxFrequency).last();
        }
    }

    static class Node {
        private final int value;
        private final Stack<Integer> pushTimes;

        void push(int time) {
            pushTimes.push(time);
        }

        void pop() {
            pushTimes.pop();
        }

        Integer peek() {
            return pushTimes.peek();
        }

        int getFreq() {
            return pushTimes.size();
        }

        int getValue() {
            return value;
        }

        Node(int value, int time) {
            this.value = value;
            pushTimes = new Stack<>();
            push(time);
        }

        static Node create(int v, int t) { return new Node(v, t); }
    }
}

/**
 * Your FreqStack object will be instantiated and called as such:
 * FreqStack obj = new FreqStack();
 * obj.push(val);
 * int param_2 = obj.pop();
 */
