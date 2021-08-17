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

describe('DeliveryNote e2e test', () => {
  const deliveryNotePageUrl = '/delivery-note';
  const deliveryNotePageUrlPattern = new RegExp('/delivery-note(\\?.*)?$');
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
    cy.intercept('GET', '/api/delivery-notes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/delivery-notes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/delivery-notes/*').as('deleteEntityRequest');
  });

  it('should load DeliveryNotes', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('delivery-note');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DeliveryNote').should('exist');
    cy.url().should('match', deliveryNotePageUrlPattern);
  });

  it('should load details DeliveryNote page', function () {
    cy.visit(deliveryNotePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('deliveryNote');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', deliveryNotePageUrlPattern);
  });

  it('should load create DeliveryNote page', () => {
    cy.visit(deliveryNotePageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DeliveryNote');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', deliveryNotePageUrlPattern);
  });

  it('should load edit DeliveryNote page', function () {
    cy.visit(deliveryNotePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('DeliveryNote');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', deliveryNotePageUrlPattern);
  });

  it('should create an instance of DeliveryNote', () => {
    cy.visit(deliveryNotePageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('DeliveryNote');

    cy.get(`[data-cy="remoteId"]`).type('7860').should('have.value', '7860');

    cy.get(`[data-cy="number"]`).type('Massachusetts Islands overriding').should('have.value', 'Massachusetts Islands overriding');

    cy.get(`[data-cy="contactName"]`).type('Orchestrator').should('have.value', 'Orchestrator');

    cy.get(`[data-cy="date"]`).type('2021-08-17').should('have.value', '2021-08-17');

    cy.get(`[data-cy="periodText"]`).type('Unbranded Practical Open-source').should('have.value', 'Unbranded Practical Open-source');

    cy.get(`[data-cy="currency"]`).select('ZMW');

    cy.get(`[data-cy="total"]`).type('62587').should('have.value', '62587');

    cy.get(`[data-cy="vatIncluded"]`).should('not.be.checked');
    cy.get(`[data-cy="vatIncluded"]`).click().should('be.checked');

    cy.get(`[data-cy="discountRate"]`).type('78750').should('have.value', '78750');

    cy.get(`[data-cy="discountType"]`).select('AMOUNT');

    cy.get(`[data-cy="language"]`).select('GERMAN');

    cy.get(`[data-cy="pageAmount"]`).type('22695').should('have.value', '22695');

    cy.get(`[data-cy="notes"]`).type('Ireland').should('have.value', 'Ireland');

    cy.get(`[data-cy="status"]`).select('SENT');

    cy.get(`[data-cy="created"]`).type('2021-08-16T13:33').should('have.value', '2021-08-16T13:33');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.setFieldSelectToLastOfEntity('contactAddress');

    cy.setFieldSelectToLastOfEntity('contactPerson');

    cy.setFieldSelectToLastOfEntity('contactPrePageAddress');

    cy.setFieldSelectToLastOfEntity('layout');

    cy.setFieldSelectToLastOfEntity('layout');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', deliveryNotePageUrlPattern);
  });

  it('should delete last instance of DeliveryNote', function () {
    cy.visit(deliveryNotePageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('deliveryNote').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deliveryNotePageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
