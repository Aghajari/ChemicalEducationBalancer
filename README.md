## ChemicalEducationBalancer
 A Java library for parsing and balancing chemical equations.

## Usage
first import ChemicalEducationBalancer.jar to your project.

```java
try {
    System.out.println(ChemicalEducationBalancer.balance("N2 + H2 = NH3").getResult());
} catch (BalancerException e) {
    e.printStackTrace();
}
```

Input : `N2 + H2 = NH3`

Output : `N2 + 3H2 → 2NH3`

Tree of BalancerSpan :
```
- [Equation]
--- [Term]
------ N (Element)
-------- 2 (Sub)
--- + (Plus)
--- 3 (Coefficient)
--- [Term]
------ H (Element)
-------- 2 (Sub)
--- → (RightArrow)
--- 2 (Coefficient)
--- [Term]
------ N (Element)
------ H (Element)
-------- 3 (Sub)
```

## Syntax
|Feature|Input|Equation|
| ------------------------------------------- | -------------------------------- | -------------------------------- |
|Subscripts| N = N2 | N → N2 |
|Compounds| H2 + O2 = H2O | H2 + O2 → H2O |
|Groups|	Mg(OH)2 = MgO + H2O |	Mg(OH)2 → MgO + H2O	|
|Ions|	H^+ + CO3^2- = H2O + CO2 |	H+ + CO32− → H2O + CO2	|
|Electrons|	Fe^3+ + e = Fe |	Fe3+ + e− → Fe	|
|No space|	A3^-+B2^2+=A5B+e |	A3− + B22+ → A5B + e−	|
|More space|	C 3 H 5 ( O H ) 3 + O 2 = H 2 O + C O 2 |	C3H5(OH)3 + O2 → H2O + CO2	|
|Optional 1|	H1^1+ + e = H1^1-	| H+ + e− → H−	|
|Flexible names|	Foo^5+ + Bar^3- = FooBar2 + FooBar^-	| Foo5+ + Bar3− → FooBar2 + FooBar−	|

## Chemical Educations
also try these educations :
```
H2 + O2 = H2O
Fe + O2 = Fe2O3
NH3 + O2 = N2 + H2O
C2H2 + O2 = CO2 + H2O
C3H8O + O2 = CO2 + H2O
Na + O2 = Na2O
P4 + O2 = P2O5
Na2O + H2O = NaOH
Mg + HCl = MgCl2 + H2
AgNO3 + LiOH = AgOH + LiNO3
Pb + PbO2 + H^+ + SO4^2- = PbSO4 + H2O
HNO3 + Cu = Cu(NO3)2 + H2O + NO
KNO2 + KNO3 + Cr2O3 = K2CrO4 + NO
AgNO3 + BaCl2 = Ba(NO3)2 + AgCl
Cu(NO3)2 = CuO + NO2 + O2
Al + CuSO4 = Al2(SO4)3 + Cu
Na3PO4 + Zn(NO3)2 = NaNO3 + Zn3(PO4)2
Cl2 + Ca(OH)2 = Ca(ClO)2 + CaCl2 + H2O
CHCl3 + O2 = CO2 + H2O + Cl2
H2C2O4 + MnO4^- = H2O + CO2 + MnO + OH^-
H2O2 + Cr2O7^2- = Cr^3+ + O2 + OH^-
KBr + KMnO4 + H2SO4 = Br2 + MnSO4 + K2SO4 + H2O
K2Cr2O7 + KI + H2SO4 = Cr2(SO4)3 + I2 + H2O + K2SO4
KClO3 + KBr + HCl = KCl + Br2 + H2O
Ag + HNO3 = AgNO3 + NO + H2O
P4 + OH^- + H2O = H2PO2^- + P2H4
Zn + NO3^- + H^+ = Zn^2+ + NH4^+ + H2O
ICl + H2O = Cl^- + IO3^- + I2 + H^+
AB2 + AC3 + AD5 + AE7 + AF11 + AG13 + AH17 + AI19 + AJ23 = A + ABCDEFGHIJ
```

## Author 
- **Amir Hossein Aghajari**
- [Chemical equation balancer (JavaScript) by Nayuki](https://www.nayuki.io/page/chemical-equation-balancer-javascript)

License
=======

    Copyright 2020 Amir Hossein Aghajari
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


<br><br>
<div align="center">
  <img width="64" alt="LCoders | AmirHosseinAghajari" src="https://user-images.githubusercontent.com/30867537/90538314-a0a79200-e193-11ea-8d90-0a3576e28a18.png">
  <br><a>Amir Hossein Aghajari</a> • <a href="mailto:amirhossein.aghajari.82@gmail.com">Email</a> • <a href="https://github.com/Aghajari">GitHub</a>
</div>
