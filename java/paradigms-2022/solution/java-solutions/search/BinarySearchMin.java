package search;

public class BinarySearchMin {
    // Let convex(a):
    // a = concat(b, c)
    // for all j (1 <= j < b.length):  b[j] < b[j - 1]
    // for all k (1 <= k < c.length):  c[k] > c[k - 1]

    // Let a[l:r] = b:
    // b.length = r - l && for all i (0 <= i < b.length): b[i] = a[i + l]

    // Let (a in b):
    // exists (0 <= i <= b.length - a.length):
    // for all j (0 <= j <= a.length): a[j] = b[i + j]

    // Lemma 1: convex(a) && 0 <= l < r <= a.length ----> convex(a[l:r])
    // Proof:
    // convex(a) --> a = concat(b, c):
    // for all j (1 <= j < b.length): b[j] < b[j - 1]
    // for all k (1 <= k < c.length): c[k] > c[k - 1]

    // if(r < b.length) --> (a[l:r] in b) -> a[l:r] = b' + c': b' = a[l:r], c = []
    // if(l >= b.length)--> (a[l:r] in c) -> a[l:r] = b' + c': b' = [], c' = a[l:r]
    // else:
    // a[l:r] = b' + c': b' = a[l:b.length], c' = [b.length : r]
    // convex(a[l:r]) == true


    // Let minValue(a):
    // for all i (0 <= i < a.length): minValue(a) <= a[i] &&
    //  exists j (0 <= j < a.length): a[j] = minValue(a)


    // Pred: args.length = a.length
    // for all i (0 <= i < a.length):  a[i] = Integer(args[i]) &&
    // convex(a)
    public static void main(String[] args) {
        int[] a = new int[args.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        int recursiveMin = recursiveMinSearch(a, 0, a.length);
        int iterativeMin = iterativeMinSearch(a);
        if (recursiveMin != iterativeMin) {
            throw new AssertionError("recursiveMin != iterativeMin");
        }
        //Post: recursiveMin == iterativeMin
        System.out.println(iterativeMin);
    }
    // Post: R = minValue(a)


    //Pred: convex(a)
    private static int iterativeMinSearch(final int[] a) {
        int l = 0, r = a.length;
        // Invariant: convex(a[l':r']) && l' < r' && a' = a &&
        // (exists j (l' <= j < r'): minValue(a) = a[j])
        while (l + 1 < r) {
            //Pred: l' + 1 < r'
            int m = (l + r) / 2;
            // m = (l' + r') / 2;
            // l' + 1 < r' ---> l' + 2 <= r' && l' <= r' - 2

            // l' < l' + 1 = (l' + (l' + 2)) / 2 <= (l' + r') / 2 <= ((r' - 2) + r') / 2 = r' - 1 < r'
            // l' < m < r' && Invariant
            if (a[m] > a[m - 1]) {
                // Pred: a[m] > a[m - 1] && m < r' && Invariant

                //1) exists j (l' <= j < r'):  a[j] == minValue(a)

                // convex(a) --> for all i (m <= i < a.length): a[i] > a[i - 1] -->
                // for all i (m <= i < a.length):  a[i] > a[m-1] >= minValue(a) -->
                // for all i (m <= i < a.length): a[i] != minValue(a) -->
                //2) for all i (m <= i < r'): a[i] != minValue(a)

                // 1) && 2) -->
                // exists j (l' <= j < m): minValue(a) = a[j]
                // convex(a[l':m])

                r = m;
                // Post: r'' = m  && l'' = l' && Invariant
            } else {
                // Pred: a[m] <= a[m - 1] && l' < m && Invariant

                //1) exists j: l' <= j < r'  a[j] == minValue(a)

                // convex(a) --> for all i (0 <= i < m): a[i] < a[i - 1] -->
                // for all i (0 <= i < m):  a[i] > a[m] >= minValue(a) -->
                // for all i (0 <= i < m): a[i] != minValue(a) -->
                //2) for all i (l' <= i < m): a[i] != minValue(a)

                // 1) && 2) --> exists j (m <= j < r'): minValue(a) = a[j] &&
                // convex(a[m:r']) (Lemma 1)

                l = m;
                // Post: l'' = m  && r'' = r' && Invariant
            }
        }
        //Pred: l < r && l + 1 >= r  -->
        // l + 1 == r

        // l + 1 == r && exists j (l <= j < r): minValue(a) = a[j] -->
        // j = l --> minValue(a) = a[l]

        return a[l];
    }
    //Post: R = minValue(a)


    //Pred: 0 <= l < r <= a.length && convex(a[l:r])
    //Post: R = minValue(a[l:r]) && a' = a
    private static int recursiveMinSearch(int[] a, int l, int r) {
        if (l + 1 == r) {
            // l + 1 == r && exists j (l <= j < r): minValue(a[l:r]) = a[j] -->
            // j = l --> minValue(a[l:r]) = a[l]
            return a[l];
            // Post R = minValue(a[l:r]) && a' = a
        }
        int m = (l + r) / 2;
        // l + 1 < r ---> l + 2 <= r && l <= r - 2
        // l < l + 1 = (l + (l + 2)) / 2 <= (l + r) / 2 <= ((r - 2) + r) / 2 = r - 1 < r  --->
        // l < m < r
        if (a[m] > a[m - 1]) {
            // Pred: a[m] > a[m - 1] && m < r && convex(a[l:r])

            //1) exists j: l <= j < r  a[j] == minValue(a[l:r])

            // convex(a[l:r]) --> for all i (m <= i < r): a[i] > a[i-1] -->
            // for all i (m <= i < r):  a[i] > a[m-1] >= minValue(a[l:r]) -->
            //2) for all i (m <= i < r): a[i] != minValue(a[l:r]) -->

            // 1) && 2) -->
            // exists j (l <= j < m): minValue(a[l:r]) = a[j] -->
            // --> minValue(a[l:m]) = minValue(a[l:r])

            // convex(a[l:m])(Lemma 1) && l < m && minValue(a[l:m]) = minValue(a[l:r])
            return recursiveMinSearch(a, l, m);
            //Post: R = minValue(a[l:m]) = minValue(a[l:r]) && a' = a
        } else {
            // Pred: a[m] <= a[m - 1] && l < m && Invariant

            //1) exists j: l <= j < r  a[j] == minValue(a[l:r])

            // convex(a[l:r]) --> for all i (l <= i < m): a[i] < a[i - 1] -->
            // for all i (l <= i < m):  a[i] > a[m] >= minValue(a) -->
            // for all i (l <= i < m): a[i] != minValue(a) -->
            //2) for all i (l <= i < m): a[i] != minValue(a)

            // 1) && 2) -->
            // exists j (m <= j < r): minValue(a[l:r]) = a[j] -->
            // --> minValue(a[m:r]) == minValue(a[l:r]) &&
            // convex(a[m:r'])(Lemma 1) && m < r && minValue(a[m:r']) == minValue(a[l:r])
            return recursiveMinSearch(a, m, r);
            //Post: R = minValue(a[m:r]) = minValue(a[l:r] && a' = a
        }
    }
}
