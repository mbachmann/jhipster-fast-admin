import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('CatalogCategory e2e test', () => {
  const catalogCategoryPageUrl = '/catalog-category-fa';
  const catalogCategoryPageUrlPattern = new RegExp('/catalog-category-fa(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/catalog-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalog-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalog-categories/*').as('deleteEntityRequest');
  });

  it('should load CatalogCategories', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalog-category-fa');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CatalogCategory').should('exist');
    cy.url().should('match', catalogCategoryPageUrlPattern);
  });

  it('should load details CatalogCategory page', function () {
    cy.visit(catalogCategoryPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('catalogCategory');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogCategoryPageUrlPattern);
  });

  it('should load create CatalogCategory page', () => {
    cy.visit(catalogCategoryPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogCategory');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogCategoryPageUrlPattern);
  });

  it('should load edit CatalogCategory page', function () {
    cy.visit(catalogCategoryPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogCategory');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogCategoryPageUrlPattern);
  });

  it('should create an instance of CatalogCategory', () => {
    cy.visit(catalogCategoryPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogCategory');

    cy.get(`[data-cy="remoteId"]`).type('99374').should('have.value', '99374');

    cy.get(`[data-cy="name"]`).type('Nevada River').should('have.value', 'Nevada River');

    cy.get(`[data-cy="accountingAccountNumber"]`).type('Quality invoice Assistant').should('have.value', 'Quality invoice Assistant');

    cy.get(`[data-cy="usage"]`).type('95932').should('have.value', '95932');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogCategoryPageUrlPattern);
  });

  it('should delete last instance of CatalogCategory', function () {
    cy.visit(catalogCategoryPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('catalogCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogCategoryPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
