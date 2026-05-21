// Client-side numerical solver ported from loesung.java
window.solver = (function(){
  function fVonX(a,b,x){ return a*x + b; }
  function gVonX(c,d,x){ return c * Math.pow(d, x); }

  function nextX(x){ return x + 1.0; }

  function hVonX(a,b,c,d){
    let x = 0.0;
    let vorher = fVonX(a,b,x) - gVonX(c,d,x);
    const maxIter = 100000;
    for(let i=0;i<maxIter;i++){
      let naechstX = nextX(x);
      let aktuell = fVonX(a,b,naechstX) - gVonX(c,d,naechstX);
      if (vorher * aktuell < 0) return [x, naechstX];
      x = naechstX;
      vorher = aktuell;
    }
    return null;
  }

  function bisectRoot(a,b,c,d,left,right,tol=1e-12,maxIter=1000){
    let fl = fVonX(a,b,left) - gVonX(c,d,left);
    let fr = fVonX(a,b,right) - gVonX(c,d,right);
    if (Math.abs(fl) <= tol) return left;
    if (Math.abs(fr) <= tol) return right;
    if (fl * fr > 0) return null;
    let l = left, r = right;
    for(let i=0;i<maxIter;i++){
      let m = (l+r)/2.0;
      let fm = fVonX(a,b,m) - gVonX(c,d,m);
      if (Math.abs(fm) <= tol) return m;
      let flocal = fVonX(a,b,l) - gVonX(c,d,l);
      if (flocal * fm < 0) r = m; else l = m;
    }
    return (l+r)/2.0;
  }

  return { fVonX, gVonX, hVonX, bisectRoot };
})();
