//package fm_index;
//
//
//import java.util.LinkedHashSet;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import javafx.util.Pair;
//
//import java.util.Collections;
//import java.util.Comparator;
//
//public class DC3SuffixArray {
//    public DC3SuffixArray(String s) {
//
//    }
//    public String getBwt() {
//        return "";
//    }
//    public static List<String> construct_suffix_array( List<String> input ) {
//
//        int v = 3;
//        int n = input.size();
//
//
//        // calculate B_k
//
//        List<Integer> B_0 = new ArrayList<Integer>();
//        List<Integer> B_1 = new ArrayList<Integer>();
//        List<Integer> B_2 = new ArrayList<Integer>();
//
//        for ( int i = 0; i <= n; i++ ) {
//            if (i % v == 0) B_0.add( i );
//            else if (i % v == 1) B_1.add( i );
//            else B_2.add( i );
//        }
//
//
//        // calculate C
//
//        List<Integer> C = new ArrayList<Integer>();
//        C.addAll( B_1 );
//        C.addAll( B_2 );
//
//
//        // append null values to input
//
//        for (int i = 0; i < v; i++) input.add("$");
//
//
//        // calculate R_k
//        List<String> R_1 = new ArrayList<String>();
//        List<String> R_2 = new ArrayList<String>();
//
//        for ( int i = 0; i < B_1.size(); i++ ) {
//            String suffix = "";
//            for (int j = 0; j < v; j++)
//                suffix = suffix + input.get( 1 + v*i + j );
//            R_1.add( suffix );
//        }
//
//        for ( int i = 0; i < B_2.size(); i++ ) {
//            String suffix = "";
//            for (int j = 0; j < v; j++)
//                suffix = suffix + input.get( 2 + v*i + j );
//            R_2.add( suffix );
//        }
//
//
//        // calculate R
//
//        List<String> R = new ArrayList<String>();
//        R.addAll( R_1 );
//        R.addAll( R_2 );
//
//
//        // sort R
//
//        List<String> R_sorted = radixSort( R );
//
//
//        // calculate R'
//
//        List<String> R_prime = new ArrayList<String>();
//        for ( int i = 0; i < ( B_1.size() + B_2.size() ); i++ ) {
//            int pos = R_sorted.indexOf( R.get( i ) );
//            R_prime.add( i, String.valueOf( pos ) );
//        }
//
//
//        // recursion
//
//        Set<String> R_prime_set = new HashSet<String>( R_prime );
//        if( R_prime_set.size() < R_prime.size() ) R_prime = construct_suffix_array( R_prime );
//
//
//        // calculate ranks
//
//        int[] ranks = new int[n+3];
//
//        for (int i = 0; i < ranks.length-2; i++) {
//            if (B_0.contains(i)) ranks[i] = -1;
//            if (B_1.contains(i)) ranks[i] = R_prime.indexOf( String.valueOf( B_1.indexOf(i) ) );
//            if (B_2.contains(i)) ranks[i] = R_prime.indexOf( String.valueOf( B_2.indexOf(i) + B_1.size() ) );
//        }
//
//
//        // merge and sort suffixes
//
//        List<Pair<Integer,Integer>> S = new ArrayList<Pair<Integer,Integer>>();
//        for ( int i = 0; i < B_0.size(); i++ ) S.add( new Pair<Integer,Integer> ( B_0.get(i), ranks[ B_0.get(i) + 1 ] ) );
//        for ( int i = 0; i < B_1.size(); i++ ) S.add( new Pair<Integer,Integer> ( B_1.get(i), ranks[ B_1.get(i) + 1 ] ) );
//        for ( int i = 0; i < B_2.size(); i++ ) S.add( new Pair<Integer,Integer> ( B_2.get(i), ranks[ B_2.get(i) + 2 ] ) );
//        radixSort( S, input );
//
//
//        // store result in list and return it
//
//        List<String> result = new ArrayList<String>();
//        for ( int i = 0; i < S.size(); i++ ) result.add( String.valueOf( S.get(i).getKey() ) );
//
//        return result;
//
//    }
//
//    public static List<String> radixSort( List<String> input ) {
//
//        List<String> result = new ArrayList<String>( input );
//
//        final int BUCKETS = 256;
//        int maxLen = Collections.max( input, Comparator.comparing( String::length )).length();
//
//        Set<String>[] wordsByLength = new LinkedHashSet[ maxLen + 1 ];
//        Set<String>[] buckets = new LinkedHashSet[ BUCKETS ];
//
//        for( int i = 0; i < wordsByLength.length; i++ )
//            wordsByLength[ i ] = new LinkedHashSet<String>();
//
//        for( int i = 0; i < BUCKETS; i++ )
//            buckets[ i ] = new LinkedHashSet<String>();
//
//        for( String s : result )
//            wordsByLength[ s.length() ].add( s );
//
//        int idx = 0;
//        for( Set<String> wordList : wordsByLength )
//            for( String s : wordList )
//                result.set( idx++, s );
//
//        int startingIndex = result.size();
//        for( int pos = maxLen - 1; pos >= 0; pos-- ) {
//
//            startingIndex -= wordsByLength[ pos + 1 ].size();
//
//            for( int i = startingIndex; i < result.size(); i++ )
//                buckets[ result.get( i ).charAt( pos ) ].add( result.get( i ) );
//
//            idx = startingIndex;
//            for( Set<String> thisBucket : buckets ) {
//                for( String s : thisBucket )
//                    result.set( idx++, s );
//
//                thisBucket.clear( );
//            }
//        }
//
//        return new ArrayList<String>( new LinkedHashSet<String> ( result ) );
//    }
//
//    public static void radixSort( List<Pair<Integer,Integer>> pairs, List<String> input ) {
//
//        final int BUCKETS = 256;
//
//        Set<Pair<Integer,Integer>> [ ] buckets = new LinkedHashSet[ BUCKETS ];
//
//        for( int i = 0; i < BUCKETS; i++ ) buckets[i] = new LinkedHashSet<>();
//
//        for( int pos = 2; pos >= 0; pos-- ) {
//
//            for( Pair<Integer,Integer> p : pairs ) {
//                String s = input.get( p.getKey() ) + input.get( p.getKey() + 1 ) + String.valueOf( p.getValue() );
//                buckets[ s.charAt( pos ) ].add( p );
//            }
//
//            int idx = 0;
//            for( Set<Pair<Integer,Integer>> thisBucket : buckets ) {
//                for( Pair<Integer,Integer> p : thisBucket ) pairs.set( idx++, p );
//                thisBucket.clear( );
//            }
//        }
//
//    }
//
//    private static void sortPairs( List<Pair<Integer,Integer>> pairs, List<String> input ) {
//
//        Collections.sort( pairs, (a, b) -> {
//            String a_t_ij = input.get( a.getKey() ) + input.get( a.getKey() + 1 );
//            String b_t_ij = input.get( b.getKey() ) + input.get( b.getKey() + 1 );
//            int cmp1 = a_t_ij.compareTo( b_t_ij );
//            if (cmp1 != 0)
//                return cmp1;
//            else
//                return a.getValue().compareTo( b.getValue() );
//        });
//    }
//
//}
