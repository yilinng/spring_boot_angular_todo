describe('Todo search', () => {
  const searchTerm = 'test';

  beforeEach(() => {
    cy.visit('/');
  });

  it('searches for a term', () => {
    cy.byTestId('search-term-input').first().clear().type(searchTerm);

    cy.byTestId('todo-item-link')
      .should('have.length', 3)
      .each((link) => {
        expect(link).to.contain(searchTerm);
        expect(link.attr('href')).to.contain('/todos/');
      });
    
  });

  it('shows the details', () => {
    cy.byTestId('search-term-input').first().clear().type(searchTerm);

    cy.byTestId('todo-item-link').first().click();

    cy.location('pathname').should('match', /\/todos/)
    
    cy.byTestId('todo-title').should('contain', searchTerm);
    cy.byTestId('todo-content').should('exist');
    cy.byTestId('todo-date').should('exist');
    cy.byTestId('todo-btn').should('exist');
  });
});