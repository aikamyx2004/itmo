module HW0.T4
  ( fac
  , fib
  , map'
  , repeat'
  ) where

import Data.Function (fix)
import Numeric.Natural (Natural)

repeat' :: a -> [a]
repeat' x = fix (x:)

map' :: (a -> b) -> [a] -> [b]
map' f = fix $ \rec arr -> case arr of
              []     -> []
              (x:xs) -> f x : rec xs

fib :: Natural -> Natural
fib n = fix (\rec k a b -> if k == 0
  then b
  else rec (k - 1) (a + b) a) n 1 0

fac :: Natural -> Natural
fac = fix $ \rec n -> if n == 0
  then 1
  else n * rec (n - 1)
