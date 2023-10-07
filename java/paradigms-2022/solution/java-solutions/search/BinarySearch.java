package search;

public class BinarySearch {
    // Let descending(a):
    // for all i (1 <= i < a.length) a[i] >= a[i-1]

    // Let a[l:r] = b:
    // b.length = r - l && for all i (0 <= i < b.length): b[i] = a[i+l]

    // Let binarySearch(x, a) = ind:
    // if(for all i (0 <= i < a.length): a[i] > x): ind = a.length
    // else: 0 <= ind < a.length
    // a[ind] <= x && (ind == 0 || a[ind - 1] > x)

    // Pred: x = Integer.parseInt(args[0]) &&
    // a.length = args.length - 1
    // for all i (0 <= i < a.length) a[i] = Integer(args[i]) &&
    // descending(a)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 0; i < a.length; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }
        if (a.length == 0 || a[0] <= x) {
            System.out.println(0);
            return;
        }
        int iterativeSearch = iterativeBinarySearch(x, a);
        int recursiveSearch = recursiveBinarySearch(x, a, 0, a.length);
        if (iterativeSearch != recursiveSearch) {
            throw new AssertionError("iterativeSearch != recursiveSearch");
        }
        // iterativeSearch == recursiveSearch
        System.out.println(iterativeSearch);
    }
    // R = binarySearch(x, a)

    //Pred: descending(a[l:r]) && 0 <= l < r < a.length && a[l] > x && (r == a.length || x >= a[r])
    private static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        if (l + 1 == r) {
            // a[l] > x && (r == a.length || x >= a[r]) ->
            // binarySearch(x, a) = r
            return r;
            //
        }
        int m = (l + r) / 2;
        // l + 1 < r ---> l + 2 <= r && l <= r - 2
        // l < l + 1 = (l + (l + 2)) / 2 <= (l + r) / 2 <= ((r - 2) + r) / 2 = r - 1 < r  --->
        // l < m < r
        if (a[m] > x) {
            // Pred: descending(a[l:r]) && l < m < r &&
            // a[l] > x && (r == a.length || x >= a[r]) && a[m] > x

            // descending(a[l:r]) -->
            // for all i (l <= i < m): a[i] > x -->
            // for all i (l <= i < m): i != binarySearch(x, a[l:r]) -->
            // binarySearch(x, a[l:r]) = binarySearch(x, a[m:r])
            return recursiveBinarySearch(x, a, m, r);
            //Post: R = binarySearch(x, a[l:r])
        } else {
            // Pred: descending(a[l:r]) && l < m < r &&
            // a[l] > x && (r == a.length || x >= a[r]) && a[m] <= x

            // descending(a[l:r]) -->
            // for all i (m <= i < r): a[i] <= x -->
            // for all i (m < i < r): i != binarySearch(x, a[l:r]) -->
            // binarySearch(x, a[l:r]) = binarySearch(x, a[l:m])
            return recursiveBinarySearch(x, a, l, m);
            //Post: R = binarySearch(x, a[l:r])
        }
    }

    //Pred: descending(a) && a[0] > x
    private static int iterativeBinarySearch(int x, int[] a) {
        int l = 0, r = a.length;
        //Invariant: descending(a[l':r']) && l' < r' && a[l'] > x && (r' == a.length || x >= a[r'])
        while (l + 1 < r) {
            int m = (l + r) / 2;
            // m = (l' + r') / 2
            // l' + 1 < r' ---> l' + 2 <= r' && l' <= r' - 2
            // l' < l' + 1 = (l' + (l' + 2)) / 2 <= (l' + r') / 2 <= ((r' - 2) + r') / 2 = r' - 1 < r'  --->
            // l' < m < r'
            if (a[m] > x) {
                // Pred: descending(a[l':r']) && l' < m < r' &&
                // a[l'] > x && (r' == a.length || x >= a[r']) && a[m] > x

                // descending(a[l':r']) -->
                // for all i (l' <= i < m): a[i] > x -->
                // for all i (l' <= i < m): i != binarySearch(x, a[l':r']) -->
                // binarySearch(x, a[l':r']) = binarySearch(x, a[m:r'])
                l = m;
            } else {
                // Pred: descending(a[l':r']) && l' < m < r' &&
                // a[l] > x && (r' == a.length || x >= a[r']) && a[m] <= x

                // descending(a[l':r']) -->
                // for all i (m <= i < r'): a[i] <= x -->
                // for all i (m < i < r'): i != binarySearch(x, a[l':r']) -->
                // binarySearch(x, a[l':r']) = binarySearch(x, a[l':m])
                r = m;
            }
        }
        // Pred: l' < r' && l' + 1 >= r' -->
        // --> l' + 1 = r'
        // a[l] > x && && (r' == a.length || x >= a[r']) -->
        // --> binarySearch(x, a) = r;
        return r;
    }
    // Post: R = binarySearch(x, a)
}
