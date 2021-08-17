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

describe('CatalogService e2e test', () => {
  const catalogServicePageUrl = '/catalog-service';
  const catalogServicePageUrlPattern = new RegExp('/catalog-service(\\?.*)?$');
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
    cy.intercept('GET', '/api/catalog-services+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalog-services').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalog-services/*').as('deleteEntityRequest');
  });

  it('should load CatalogServices', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalog-service');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CatalogService').should('exist');
    cy.url().should('match', catalogServicePageUrlPattern);
  });

  it('should load details CatalogService page', function () {
    cy.visit(catalogServicePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('catalogService');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogServicePageUrlPattern);
  });

  it('should load create CatalogService page', () => {
    cy.visit(catalogServicePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogService');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogServicePageUrlPattern);
  });

  it('should load edit CatalogService page', function () {
    cy.visit(catalogServicePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogService');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogServicePageUrlPattern);
  });

  it('should create an instance of CatalogService', () => {
    cy.visit(catalogServicePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogService');

    cy.get(`[data-cy="remoteId"]`).type('89651').should('have.value', '89651');

    cy.get(`[data-cy="number"]`).type('XSS').should('have.value', 'XSS');

    cy.get(`[data-cy="name"]`).type('Front-line Branding').should('have.value', 'Front-line Branding');

    cy.get(`[data-cy="description"]`).type('Ergonomic Uzbekistan').should('have.value', 'Ergonomic Uzbekistan');

    cy.get(`[data-cy="notes"]`).type('Plains Account').should('have.value', 'Plains Account');

    cy.get(`[data-cy="categoryName"]`).type('Rufiyaa').should('have.value', 'Rufiyaa');

    cy.get(`[data-cy="includingVat"]`).should('not.be.checked');
    cy.get(`[data-cy="includingVat"]`).click().should('be.checked');

    cy.get(`[data-cy="vat"]`).type('940').should('have.value', '940');

    cy.get(`[data-cy="unitName"]`).type('Checking Platinum utilize').should('have.value', 'Checking Platinum utilize');

    cy.get(`[data-cy="price"]`).type('5326').should('have.value', '5326');

    cy.get(`[data-cy="defaultAmount"]`).type('27598').should('have.value', '27598');

    cy.get(`[data-cy="created"]`).type('2021-08-17T04:01').should('have.value', '2021-08-17T04:01');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.setFieldSelectToLastOfEntity('category');

    cy.setFieldSelectToLastOfEntity('unit');

    cy.setFieldSelectToLastOfEntity('valueAddedTax');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogServicePageUrlPattern);
  });

  it('should delete last instance of CatalogService', function () {
    cy.visit(catalogServicePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('catalogService').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogServicePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
