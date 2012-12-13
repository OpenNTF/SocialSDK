require(["sbt/_bridge/lang"], function(lang) {
    describe("sbt._bridge.lang", function() {

        beforeEach(function() {
        });
        
        it("Should be defined", function() {
            expect(lang).not.toBeUndefined();
        });
    
        it("Should be able to mixin", function() {
            var a = { b:"c", d:"e" };
            lang.mixin(a, { d:"f", g:"h" });
            console.log(a); // b:c, d:f, g:h
            expect(a.b).toBe("c");
            expect(a.d).toBe("f");
            expect(a.g).toBe("h");
        });
    
    });
});
