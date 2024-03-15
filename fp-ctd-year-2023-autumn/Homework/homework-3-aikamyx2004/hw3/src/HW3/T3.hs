module HW3.T3
  ( joinOption
  , joinExcept
  , joinAnnotated
  , joinList
  , joinFun
  ) where

import HW3.T1

joinOption :: Option (Option a) -> Option a
joinOption None = None
joinOption (Some s) = s

joinExcept :: Except e (Except e a) -> Except e a
joinExcept (Error e) = Error e
joinExcept (Success s) = s

joinAnnotated :: Semigroup e => Annotated e (Annotated e a) -> Annotated e a
joinAnnotated ((a :# e1) :# e2) = a :# (e2 <> e1)

concatLists :: List a -> List a -> List a
concatLists Nil ys = ys
concatLists (x :. xs) ys = x :. concatLists xs ys

joinList :: List (List a) -> List a
joinList Nil = Nil
joinList (x :. xs) = concatLists x (joinList xs)

joinFun :: Fun i (Fun i a) -> Fun i a
joinFun (F f) = F (\i -> let (F g) = f i in g i)