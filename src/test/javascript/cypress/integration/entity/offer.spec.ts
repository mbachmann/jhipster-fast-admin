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

describe('Offer e2e test', () => {
  const offerPageUrl = '/offer';
  const offerPageUrlPattern = new RegExp('/offer(\\?.*)?$');
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
    cy.intercept('GET', '/api/offers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/offers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/offers/*').as('deleteEntityRequest');
  });

  it('should load Offers', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('offer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Offer').should('exist');
    cy.url().should('match', offerPageUrlPattern);
  });

  it('should load details Offer page', function () {
    cy.visit(offerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('offer');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', offerPageUrlPattern);
  });

  it('should load create Offer page', () => {
    cy.visit(offerPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Offer');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', offerPageUrlPattern);
  });

  it('should load edit Offer page', function () {
    cy.visit(offerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('Offer');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', offerPageUrlPattern);
  });

  it('should create an instance of Offer', () => {
    cy.visit(offerPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Offer');

    cy.get(`[data-cy="remoteId"]`).type('4012').should('have.value', '4012');

    cy.get(`[data-cy="number"]`).type('Dirham').should('have.value', 'Dirham');

    cy.get(`[data-cy="contactName"]`).type('dynamic').should('have.value', 'dynamic');

    cy.get(`[data-cy="date"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="validUntil"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="periodText"]`).type('Shilling').should('have.value', 'Shilling');

    cy.get(`[data-cy="currency"]`).select('ZAR');

    cy.get(`[data-cy="total"]`).type('6469').should('have.value', '6469');

    cy.get(`[data-cy="vatIncluded"]`).should('not.be.checked');
    cy.get(`[data-cy="vatIncluded"]`).click().should('be.checked');

    cy.get(`[data-cy="discountRate"]`).type('11692').should('have.value', '11692');

    cy.get(`[data-cy="discountType"]`).select('IN_PERCENT');

    cy.get(`[data-cy="acceptOnline"]`).should('not.be.checked');
    cy.get(`[data-cy="acceptOnline"]`).click().should('be.checked');

    cy.get(`[data-cy="acceptOnlineUrl"]`).type('Baby process capacitor').should('have.value', 'Baby process capacitor');

    cy.get(`[data-cy="acceptOnlineStatus"]`).select('DECLINED');

    cy.get(`[data-cy="language"]`).select('SPANISH');

    cy.get(`[data-cy="pageAmount"]`).type('39527').should('have.value', '39527');

    cy.get(`[data-cy="notes"]`).type('Internal sensor Directives').should('have.value', 'Internal sensor Directives');

    cy.get(`[data-cy="status"]`).select('BILLED');

    cy.get(`[data-cy="created"]`).type('2021-08-17T04:03').should('have.value', '2021-08-17T04:03');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.setFieldSelectToLastOfEntity('contactAddress');

    cy.setFieldSelectToLastOfEntity('contactPerson');

    cy.setFieldSelectToLastOfEntity('contactPrePageAddress');

    cy.setFieldSelectToLastOfEntity('layout');

    cy.setFieldSelectToLastOfEntity('signature');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', offerPageUrlPattern);
  });

  it('should delete last instance of Offer', function () {
    cy.visit(offerPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('offer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', offerPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
