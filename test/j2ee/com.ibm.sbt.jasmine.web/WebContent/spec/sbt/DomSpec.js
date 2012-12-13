require(["sbt/_bridge/dom"], function(dom) {
    describe("sbt._bridge.dom", function() {

        beforeEach(function() {
        });
        
        it("Should be defined", function() {
            expect(dom).not.toBeUndefined();
        });
    
        it("Should be able to get a node by id", function() {
            var myDiv = dom.byId("myDiv");
            expect(myDiv).not.toBeUndefined();
            var noExist = dom.byId("noExist");
            expect(noExist).toBeNull();
        });
    
        it("Should be able to create a text node", function() {
            var textNode = dom.createTextNode("Text Node");
            expect(textNode).not.toBeUndefined();
            expect(textNode.nodeType).toBe(3);
            expect(textNode.textContent).toBe("Text Node");
        });
    
        it("Should be able to remove all from a node", function() {
            var myUl = dom.removeAll("myUl");
            expect(myUl).not.toBeUndefined();
            expect(myUl.hasChildNodes()).toBeFalsy();
        });
        
        it("Should be able to set an attribute on a node", function() {
            var myUl = dom.setAttr("myUl", "title", "My UL Title");
            expect(myUl).not.toBeUndefined();
            expect(myUl.getAttribute("title")).toBe("My UL Title");
        });

    });
});
