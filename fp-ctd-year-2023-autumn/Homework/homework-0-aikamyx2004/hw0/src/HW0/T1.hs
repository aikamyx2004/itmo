{-# LANGUAGE TypeOperators #-}

module HW0.T1
  ( type (<->) (Iso)
  , assocEither
  , assocPair
  , distrib
  , flipIso
  , runIso
  ) where

data a <-> b = Iso (a -> b) (b -> a)

distrib :: Either a (b, c) -> (Either a b, Either a c)
distrib (Left a)       = (Left a, Left a)
distrib (Right (b, c)) = (Right b, Right c)

flipIso :: (a <-> b) -> (b <-> a)
flipIso (Iso f g) = Iso g f

runIso :: (a <-> b) -> (a -> b)
runIso (Iso f _) = f

assocPair :: (a, (b, c)) <-> ((a, b), c)
assocPair = Iso first second
  where first (a, (b, c)) = ((a, b), c)
        second ((a, b), c) = (a, (b, c))



assocEither :: Either a (Either b c) <-> Either (Either a b) c
assocEither = Iso first second
  where
    first (Left a)          = Left (Left a)
    first (Right (Left b))  = Left (Right b)
    first (Right (Right c)) = Right c

    second (Left (Left a))  = Left a
    second (Left (Right b)) = Right (Left b)
    second (Right c)        = Right (Right c)
