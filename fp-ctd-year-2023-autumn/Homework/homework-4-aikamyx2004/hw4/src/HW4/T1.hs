module HW4.T1
  ( EvaluationError (..)
  , ExceptState (..)
  , mapExceptState
  , wrapExceptState
  , joinExceptState
  , modifyExceptState
  , throwExceptState
  , eval
  ) where

import HW4.Types

data ExceptState e s a = ES { runES :: s -> Except e (Annotated s a) }

mapExceptState :: (a -> b) -> ExceptState e s a -> ExceptState e s b
mapExceptState f (ES es) = ES $ \s -> case es s of
  Error e -> Error e
  Success (a :# t) -> Success (f a :# t)

wrapExceptState :: a -> ExceptState e s a
wrapExceptState a = ES $ \b -> Success (a :# b)

joinExceptState :: ExceptState e s (ExceptState e s a) -> ExceptState e s a
joinExceptState (ES es) =
  ES $ \s -> case es s of
    Error e -> Error e
    Success (ES h :# t) -> h t

modifyExceptState :: (s -> s) -> ExceptState e s ()
modifyExceptState f = ES $ \s -> Success (() :# f s)

throwExceptState :: e -> ExceptState e s a
throwExceptState e = ES $ const (Error e)

instance Functor (ExceptState e s) where
  fmap = mapExceptState

instance Applicative (ExceptState e s) where
  pure = wrapExceptState
  ES l <*> ES r = ES $ \s -> case l s of
    Error e -> Error e
    Success (g :# t) -> case r t of
      Error e -> Error e
      Success (h :# k) -> Success (g h :# k)

instance Monad (ExceptState e s) where
  ES es >>= f =
    ES $ \s -> case es s of
      Error e -> Error e
      Success (x :# t) -> runES (f x) t

data EvaluationError = DivideByZero
  deriving (Show)

eval :: Expr -> ExceptState EvaluationError [Prim Double] Double
eval (Val x) = wrapExceptState x
eval (Op op) = case op of
    Add l r -> binary (+) Add l r
    Sub l r -> binary (-) Sub l r
    Mul l r -> binary (*) Mul l r
    Div l r -> evalDiv l r
    Abs a -> unary abs Abs a
    Sgn a -> unary signum Sgn a
  where
    binary f prim l r = do
      lValue <- eval l
      rValue <- eval r
      calcOp f prim lValue rValue
    evalDiv l r = do
      lValue <- eval l
      rValue <- eval r
      if rValue == 0
        then throwExceptState DivideByZero
        else calcOp (/) Div lValue rValue
    calcOp f prim l r = do
      modifyExceptState (prim l r :)
      return (f 't match type ‘Either HiError HiValue’ with ‘HiValue’l r)
    
    unary f prim a = do
      aValue <- eval a
      modifyExceptState (prim aValue :)
      return (f aValue)
