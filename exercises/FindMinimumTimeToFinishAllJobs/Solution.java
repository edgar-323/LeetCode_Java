class Solution {
    public int minimumTimeRequired(int[] jobs, int k) {
        return Solution2.newSolver(jobs, k).solve();
    }

    /**
     * THOUGHTS:
     * Every job has to get done sooner or later.
     * At the end, every job is going to have an assignment which is one of the
     * k workers (which we can think of as buckets).
     * From the description, it does appear that all k buckets are equivalent which
     * means that they operate at the same rate and that they can operate in parallel.
     * Once every job has been assigned a bucket, the total time it takes to complete
     * all jobs will be equal to the bucket with the maximum runtime.
     * Do we have to keep track of buckets at any given time?
     * If you think about it, the task is to minimize the maximum of the buckets.
     * In this case, it does sound like we need to always track the jobs.
     *
     * What state representation should we use?
     * What about: (unscheduledJobs, buckets[])
     * In the above representation, the unscheduledJobs will contain a list of indexes
     * of jobs that still need to be assigned to a bucket.
     * While, buckets[] will contain the total time that the j-th bucket will take.
     * Note that the total number of jobs will be limited to 12 which means we can
     * use a single integer to represent unscheduledJobs.
     * Can we reduce buckets[] to a more friendly representation?
     * Unfortunately, I don't think so but once we attempt to solve this problem
     * we will check the solutions ...
     * What about hueristics?
     * Well, the buckets are symmetrical so their ordering is not relevant.
     * Let's see... Ideally we would want to distribute the jobs evenly amongst workers.
     * So (assumming k <= length(jobs)), they would each have cost:
     *              lower_bound_cost = ceil(sum(jobs[]) / k)
     * The above is a lower bound and ideally it is a lot different because a single
     * job can have cost 9999, while the rest of the jobs can cost 1.
     * So can we the lower_bound_cost in order to generate any hueristics?
     * What about, moving onto the next bucket as soon as we surpass this threshold?
     * More generally, it would be good to make buckets[] a cache-able parameter.
     * Actually, we can just keep an index of the bucket that can accept a job at present.
     * We will always incremement this parameter modulo k. This *should* work because
     * in the optimal solution, every bucket needs to be assigned at least one of the
     * jobs.
     */

    static class Solution1 {
        /**
         * THIS APPROACH TIMES OUT.
         */

        private final int N;
        private final int k;
        private final int[] jobs;
        private final int[] buckets;

        int solve() {
            int unscheduledJobs = 0;
            for (int job = 0; job < N; job++) {
                unscheduledJobs = markJobUnscheduled(unscheduledJobs, job);
            }

            return solve(unscheduledJobs);
        }

        int solve(int unscheduledJobs) {
            if (allJobsScheduled(unscheduledJobs)) {
                return calculateMaxBucketCost();
            }

            int minCost = Integer.MAX_VALUE;
            for (int job = 0; job < N; job++) {
                if (!jobNeedsToBeScheduled(unscheduledJobs, job)) {
                    continue;
                }
                for (int bucket = 0; bucket < k; bucket++) {
                    buckets[bucket] += jobs[job];
                    minCost = Math.min(
                            minCost,
                            solve(markJobScheduled(unscheduledJobs, job)));
                    buckets[bucket] -= jobs[job];
                }
            }

            return minCost;
        }

        boolean allJobsScheduled(int unscheduledJobs) {
            return unscheduledJobs == 0;
        }

        boolean jobNeedsToBeScheduled(int unscheduledJobs, int job) {
            return (unscheduledJobs & (1 << job)) != 0;
        }

        int markJobScheduled(int unscheduledJobs, int job) {
            return (unscheduledJobs & (~(1 << job)));
        }

        int markJobUnscheduled(int unscheduledJobs, int job) {
            return (unscheduledJobs | (1 << job));
        }

        int calculateMaxBucketCost() {
            return Arrays.stream(buckets).max().getAsInt();
        }

        int nextBucket(int bucket) {
            return (bucket + 1) % k;
        }

        static Solution1 newSolver(int[] jobs, int k) {
            return new Solution1(jobs, k);
        }

        Solution1(int[] jobs, int k) {
            this.jobs = jobs;
            this.buckets = new int[k];
            this.k = k;
            this.N = jobs.length;
        }
    }

    static class Solution2 {
        private final Integer[] jobs;
        private final int[] buckets;
        private final int N;
        private final int K;

        private int globalMinMax;


        int solve() {
            // Sort the jobs in descending order so that larger jobs are placed first.
            // This means that we will try to schedule larger jobs first.
            Arrays.sort(jobs, Collections.reverseOrder());

            if (K >= N) {
                // Since there are more buckets than jobs, we can run all jobs in parallel.
                // The min-max will be equal to the length of the largest job.
                return jobs[0];
            }

            solve(/* job= */ 0);

            return globalMinMax;
        }

        void solve(int job) {
            if (job == N) {
                // All jobs have been scheduled.
                globalMinMax = Math.min(globalMinMax, calculateMaxBucketCost());
                return;
            }

            if (calculateMaxBucketCost() >= globalMinMax) {
                // No need to continue exploring with the current estimation since it will
                // surely be worse (larger) than our current estimation.
                return;
            }

            for (int bucket = 0; bucket < K; bucket++) {
                if (bucket > 0 && buckets[bucket] >= buckets[bucket-1]) {
                    // It's not optimal to add more work to the current bucket since it is
                    // already larger than it's left neighbor and we wanto minimize the max
                    // peak of all bucket costs.
                    continue;
                }
                buckets[bucket] += jobs[job];
                solve(job + 1);
                buckets[bucket] -= jobs[job];
            }
        }

        int calculateMaxBucketCost() {
            return Arrays.stream(buckets).max().getAsInt();
        }

        static Solution2 newSolver(int[] jobs, int k) {
            return new Solution2(jobs, k);
        }

        Solution2(int[] jobs, int k) {
            this.jobs = Arrays.stream(jobs).boxed().toArray(Integer[]::new);
            this.K = k;
            this.N = jobs.length;
            this.buckets = new int[K];

            globalMinMax = Integer.MAX_VALUE;
        } 
    }
}
