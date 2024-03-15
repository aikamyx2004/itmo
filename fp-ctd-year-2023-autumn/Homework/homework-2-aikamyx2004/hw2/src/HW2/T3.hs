module HW2.T3
  ( epart
  , mcat
  ) where

mcat :: Monoid a => [Maybe a] -> a
mcat = undefined

epart :: (Monoid a, Monoid b) => [Either a b] -> (a, b)
epart = undefined
