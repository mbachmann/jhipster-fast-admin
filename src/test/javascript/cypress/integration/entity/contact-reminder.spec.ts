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

describe('ContactReminder e2e test', () => {
  const contactReminderPageUrl = '/contact-reminder';
  const contactReminderPageUrlPattern = new RegExp('/contact-reminder(\\?.*)?$');
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
    cy.intercept('GET', '/api/contact-reminders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contact-reminders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contact-reminders/*').as('deleteEntityRequest');
  });

  it('should load ContactReminders', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contact-reminder');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContactReminder').should('exist');
    cy.url().should('match', contactReminderPageUrlPattern);
  });

  it('should load details ContactReminder page', function () {
    cy.visit(contactReminderPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityDetailsButtonSelector).first().click({ force: true });
    cy.getEntityDetailsHeading('contactReminder');
    cy.get(entityDetailsBackButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactReminderPageUrlPattern);
  });

  it('should load create ContactReminder page', () => {
    cy.visit(contactReminderPageUrl);
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactReminder');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactReminderPageUrlPattern);
  });

  it('should load edit ContactReminder page', function () {
    cy.visit(contactReminderPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        this.skip();
      }
    });
    cy.get(entityEditButtonSelector).first().click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactReminder');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.get(entityCreateCancelButtonSelector).click({ force: true });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactReminderPageUrlPattern);
  });

  it('should create an instance of ContactReminder', () => {
    cy.visit(contactReminderPageUrl);
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('ContactReminder');

    cy.get(`[data-cy="remoteId"]`).type('73797').should('have.value', '73797');

    cy.get(`[data-cy="contactId"]`).type('84256').should('have.value', '84256');

    cy.get(`[data-cy="contactName"]`).type('withdrawal synergy Forward').should('have.value', 'withdrawal synergy Forward');

    cy.get(`[data-cy="dateTime"]`).type('2021-08-17T10:56').should('have.value', '2021-08-17T10:56');

    cy.get(`[data-cy="title"]`).type('fuchsia District').should('have.value', 'fuchsia District');

    cy.get(`[data-cy="description"]`).type('SSL Peso Brazilian').should('have.value', 'SSL Peso Brazilian');

    cy.get(`[data-cy="intervalValue"]`).type('89065').should('have.value', '89065');

    cy.get(`[data-cy="intervalType"]`).select('DAY');

    cy.get(`[data-cy="inactiv"]`).should('not.be.checked');
    cy.get(`[data-cy="inactiv"]`).click().should('be.checked');

    cy.setFieldSelectToLastOfEntity('contact');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.wait('@postEntityRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(201);
    });
    cy.wait('@entitiesRequest').then(({ response }) => {
      expect(response.statusCode).to.equal(200);
    });
    cy.url().should('match', contactReminderPageUrlPattern);
  });

  it('should delete last instance of ContactReminder', function () {
    cy.visit(contactReminderPageUrl);
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', response.body.length);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.getEntityDeleteDialogHeading('contactReminder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', contactReminderPageUrlPattern);
      } else {
        this.skip();
      }
    });
  });
});
