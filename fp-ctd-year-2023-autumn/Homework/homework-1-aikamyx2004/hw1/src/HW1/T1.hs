module HW1.T1
  ( Day (..),
    afterDays,
    daysToParty,
    isWeekend,
    nextDay,
  )
where

import Numeric.Natural (Natural)

data Day
  = Monday
  | Tuesday
  | Wednesday
  | Thursday
  | Friday
  | Saturday
  | Sunday
  deriving (Show)

nextDay :: Day -> Day
nextDay Monday = Tuesday
nextDay Tuesday = Wednesday
nextDay Wednesday = Thursday  
nextDay Thursday = Friday
nextDay Friday = Saturday
nextDay Saturday = Sunday
nextDay Sunday = Monday

afterDays :: Natural -> Day -> Day
afterDays 0 x = x
afterDays n x
  | n == 0 = x
  | n >= 7 = afterDays (n `rem` 7) x
  | otherwise = afterDays (n - 1) (nextDay x)

isWeekend :: Day -> Bool
isWeekend Saturday = True
isWeekend Sunday = True
isWeekend _ = False

daysToParty :: Day -> Natural
daysToParty Friday = 0
daysToParty x = 1 + daysToParty (nextDay x)
