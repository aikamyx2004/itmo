module HW2.T2
  ( joinWith
  , splitOn
  ) where

import Data.List.NonEmpty (NonEmpty((:|)), cons, head, tail)

splitOn :: Eq a => a -> [a] -> NonEmpty [a]
splitOn = undefined

joinWith :: a -> NonEmpty [a] -> [a]
joinWith = undefined
