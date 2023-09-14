describe('dashboard', () => {
//  const searchTerm = 'test';

  beforeEach(() => {
    cy.visit('/');
  });

  it('top 5 todos', () => {

    cy.byTestId('todo-item-link')
      .should('have.length', 3)
      .each((link) => {
        expect(link.attr('href')).to.contain('/todos/');
      });
    
  });

  it('shows the details and click back button redirect to home page', () => {

    cy.byTestId('todo-item-link').first().click();

    cy.location('pathname').should('match', /\/todos/)
    
    cy.byTestId('todo-title').should('exist');
    cy.byTestId('todo-content').should('exist');
    cy.byTestId('todo-date').should('exist');
    cy.byTestId('todo-btn').should('exist');
    cy.byTestId('todo-btn').click()

    cy.get('h2:contains("Top Todos")');

  });
});