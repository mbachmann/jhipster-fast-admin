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

describe('CatalogProduct e2e test', () => {
  const catalogProductPageUrl = '/catalog-product';
  const catalogProductPageUrlPattern = new RegExp('/catalog-product(\\?.*)?$');
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
    cy.intercept('GET', '/api/catalog-products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalog-products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalog-products/*').as('deleteEntityRequest');
  });

  it('should load CatalogProducts', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalog-product');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CatalogProduct').should('exist');
    cy.url().should('match', catalogProductPageUrlPattern);
  });

  it('should load details CatalogProduct page', function () {
    cy.visit(catalogProductPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('catalogProduct');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogProductPageUrlPattern);
  });

  it('should load create CatalogProduct page', () => {
    cy.visit(catalogProductPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogProduct');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogProductPageUrlPattern);
  });

  it('should load edit CatalogProduct page', function () {
    cy.visit(catalogProductPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogProduct');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', catalogProductPageUrlPattern);
  });

  it('should create an instance of CatalogProduct', () => {
    cy.visit(catalogProductPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('CatalogProduct');

    cy.get(`[data-cy="remoteId"]`).type('4805').should('have.value', '4805');

    cy.get(`[data-cy="number"]`).type('Innovative Supervisor turquoise').should('have.value', 'Innovative Supervisor turquoise');

    cy.get(`[data-cy="name"]`).type('Clothing').should('have.value', 'Clothing');

    cy.get(`[data-cy="description"]`).type('transparent Ethiopia').should('have.value', 'transparent Ethiopia');

    cy.get(`[data-cy="notes"]`).type('feed synthesize Crescent').should('have.value', 'feed synthesize Crescent');

    cy.get(`[data-cy="categoryName"]`).type('action-items markets up').should('have.value', 'action-items markets up');

    cy.get(`[data-cy="includingVat"]`).should('not.be.checked');
    cy.get(`[data-cy="includingVat"]`).click().should('be.checked');

    cy.get(`[data-cy="vat"]`).type('92711').should('have.value', '92711');

    cy.get(`[data-cy="unitName"]`).type('encompassing driver Loan').should('have.value', 'encompassing driver Loan');

    cy.get(`[data-cy="price"]`).type('31846').should('have.value', '31846');

    cy.get(`[data-cy="pricePurchase"]`).type('64791').should('have.value', '64791');

    cy.get(`[data-cy="inventoryAvailable"]`).type('98204').should('have.value', '98204');

    cy.get(`[data-cy="inventoryReserved"]`).type('91676').should('have.value', '91676');

    cy.get(`[data-cy="defaultAmount"]`).type('91604').should('have.value', '91604');

    cy.get(`[data-cy="created"]`).type('2021-08-17T04:10').should('have.value', '2021-08-17T04:10');

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
    cy.url().should('match', catalogProductPageUrlPattern);
  });

  it('should delete last instance of CatalogProduct', function () {
    cy.visit(catalogProductPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('catalogProduct').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', catalogProductPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
