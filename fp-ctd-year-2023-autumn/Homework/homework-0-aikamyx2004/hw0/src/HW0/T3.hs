module HW0.T3
  ( compose
  , contract
  , i
  , k
  , permute
  , s
  ) where

-- S
s :: (a -> b -> c) -> (a -> b) -> (a -> c)
s f g x = f x (g x)

-- K
k :: a -> b -> a
k x _ = x

-- I
i :: a -> a
i x = x

-- B
compose :: (b -> c) -> (a -> b) -> (a -> c)
compose f g x = f (g x)

-- W
contract :: (a -> a -> b) -> (a -> b)
contract f x = f x x

-- C
permute :: (a -> b -> c) -> (b -> a -> c)
permute f x y = f y x